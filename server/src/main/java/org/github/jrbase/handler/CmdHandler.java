package org.github.jrbase.handler;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.github.jrbase.config.RedisConfigurationOption;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisClientContext;
import org.github.jrbase.handler.connect.CommandHandler;
import org.github.jrbase.handler.connect.EchoHandler;
import org.github.jrbase.handler.connect.PingHandler;
import org.github.jrbase.manager.CmdManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static org.github.jrbase.dataType.ServerCmd.*;

public class CmdHandler {

    private static final String REPLY_OK = "OK";

    /**
     * save session login status to HashMap<ChannelHandlerContext, RedisClientContext>
     */
    private final Map<ChannelHandlerContext, RedisClientContext> clientContext = new HashMap<>();

    private final Map<String, ServerCmdHandler> serverCmdHandlerMap = new HashMap<>();

    private RedisConfigurationOption redisConfigurationOption;

    public CmdHandler(RedisConfigurationOption redisConfigurationOption) {
        this.redisConfigurationOption = redisConfigurationOption;
        initHandlerMap();
    }

    private void initHandlerMap() {
        serverCmdHandlerMap.put(ECHO.getCmdName(), new EchoHandler());
        serverCmdHandlerMap.put(PING.getCmdName(), new PingHandler());
        serverCmdHandlerMap.put(COMMAND.getCmdName(), new CommandHandler());
    }

    public void handleMsg(ChannelHandlerContext ctx, String message) {

        final ClientCmd clientCmd = parseMessage(message);

        if (checkRequirePassword(ctx, clientCmd)) {
            return;
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
                clientCmd.setRheaKVStore(CmdManager.getRheaKVStore());
                CmdManager.process(clientCmd);
            }
        }

    }

    private boolean checkRequirePassword(final ChannelHandlerContext ctx, final ClientCmd clientCmd) {
        if (StringUtils.isNotEmpty(redisConfigurationOption.getRequirePass())) {
            final RedisClientContext redisClientContext = clientContext.get(ctx);
            if (dontLogin(redisClientContext)) {
                handleAuth(ctx, clientCmd, clientContext, redisClientContext);
                return true;
            }
        }
        return false;
    }

    private boolean dontLogin(RedisClientContext redisClientContext) {
        return redisClientContext == null || !redisClientContext.isLogin();
    }

    private void handleAuth(ChannelHandlerContext ctx, ClientCmd clientCmd, Map<ChannelHandlerContext, RedisClientContext> clientContext, RedisClientContext redisClientContext) {
        if (AUTH.getCmdName().equals(clientCmd.getCmd())) {
            if (authPassword(clientCmd)) {
                if (redisClientContext == null) {
                    redisClientContext = new RedisClientContext();
                    clientContext.put(ctx, redisClientContext);
                }
                redisClientContext.setLogin(true);
                replyInfoToClient(ctx, REPLY_OK);
            } else {
                replyErrorToClient(ctx, "ERR invalid password");
            }
        } else {
            replyInfoToClient(ctx, "NOAUTH Authentication required.");
        }
    }

    private boolean authPassword(ClientCmd clientCmd) {
        return redisConfigurationOption.getRequirePass().equals(clientCmd.getKey());
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
