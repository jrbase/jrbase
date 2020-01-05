package org.github.jrbase.handler;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.ChannelHandlerContext;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisClientContext;
import org.github.jrbase.handler.connect.CommandHandler;
import org.github.jrbase.handler.connect.EchoHandler;
import org.github.jrbase.handler.connect.PingHandler;
import org.github.jrbase.manager.CmdManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CmdHandler {

    private static final String REPLY_OK = "OK";
    //TODO: read config file from local system
    private boolean requirepass = false;
    private String redisPassword = "123456";

    // save session login status to HashMap<ChannelHandlerContext, RedisClientContext>
    private Map<ChannelHandlerContext, RedisClientContext> clientContext = new HashMap<>();

    private static final Map<String, ServerCmdHandler> serverCmdHandlerMap = new HashMap<>();

    public CmdHandler() {
        initHandlerMap();
    }

    private void initHandlerMap() {
        serverCmdHandlerMap.put("echo", new EchoHandler());
        serverCmdHandlerMap.put("ping", new PingHandler());
        serverCmdHandlerMap.put("command", new CommandHandler());
    }

    public void handleMsg(ChannelHandlerContext ctx, String message) {

        final ClientCmd clientCmd = parseMessage(message);
        //1. connect
        //TODO:  handleAuth();
        if (requirepass) {
            final RedisClientContext redisClientContext = clientContext.get(ctx);
            if (!redisClientContext.isLogin()) {
                handleAuth(ctx, clientCmd, redisClientContext);
                return;
            }
        }

        if (clientCmd.getCmd().isEmpty()) {
            replyErrorToClient(ctx, "empty command");
        } else {
            final ServerCmdHandler serverCmdHandler = serverCmdHandlerMap.get(clientCmd.getCmd());
            if (serverCmdHandler != null) {
                //handle server command
                final String cmdHandlerResult = serverCmdHandler.handle(clientCmd);
                ctx.channel().writeAndFlush(cmdHandlerResult);
            } else {
                //handle data command
                clientCmd.setChannel(ctx.channel());
                final RheaKVStore rheaKVStore = CmdManager.getRheaKVStore();
                clientCmd.setRheaKVStore(rheaKVStore);
                CmdManager.process(clientCmd);
            }
        }

    }

    private void handleAuth(ChannelHandlerContext ctx, ClientCmd clientCmd, RedisClientContext redisClientContext) {
        if ("auth".equals(clientCmd.getCmd())) {
            String password = clientCmd.getKey();
            if (password.equals(redisPassword)) {
                redisClientContext.setLogin(true);
                replyInfoToClient(ctx, REPLY_OK);
            } else {
                replyErrorToClient(ctx, "ERR invalid password");
            }
        } else {
            replyInfoToClient(ctx, "NOAUTH Authentication required.");
        }
    }

    ClientCmd parseMessage(String message) {
        // *3\r\n$3\r\nset\r\n$3\r\nkey$5\r\nvalue\r\n).split("\r\n")
        final String[] redisClientCmdArr = message.split("\r\n");
        return parseMessageToClientCmd(redisClientCmdArr);
    }

    //             key   value
    // 0  1  2  3  4  5  6
    //*3 $3 set $1 a $1  b
    private ClientCmd parseMessageToClientCmd(final String[] redisClientCmdArr) {
        ClientCmd clientCmd = new ClientCmd();
        clientCmd.setCmd("");
        clientCmd.setKey("");
        clientCmd.setArgs(new String[0]);

        if (isHaveCmd(redisClientCmdArr.length)) {
            clientCmd.setCmd(redisClientCmdArr[2]);
            if (isHaveKey(redisClientCmdArr.length)) {
                clientCmd.setKey(redisClientCmdArr[4]);
                if (isHaveArgs(redisClientCmdArr.length)) {
                    final String[] args = getArgs(redisClientCmdArr);
                    clientCmd.setArgs(args);
                }
            }
        }
        return clientCmd;
    }

    @NotNull
    String[] getArgs(final String[] redisClientCmdArr) {
        final int argsCount = (redisClientCmdArr.length + 1 - 6) / 2;
        String[] args = new String[argsCount];
        int count = 0;
        for (int i = 6; i < redisClientCmdArr.length; i = i + 2) {
            args[count++] = redisClientCmdArr[i];
        }
        return args;
    }

    private boolean isHaveCmd(int length) {
        return length >= 3;
    }

    private boolean isHaveKey(int length) {
        return length > 4;
    }

    private boolean isHaveArgs(int length) {
        return length > 6;
    }

    private void replyInfoToClient(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush("+" + msg + "\r\n");
    }

    private void replyErrorToClient(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush("-" + msg + "\r\n");
    }

}
