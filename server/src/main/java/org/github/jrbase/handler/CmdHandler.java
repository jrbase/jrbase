package org.github.jrbase.handler;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.github.jrbase.backend.JraftKVDecorator;
import org.github.jrbase.config.RedisConfigurationOption;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisClientContext;
import org.github.jrbase.handler.annotation.ScanServerAnnotationConfigure;
import org.github.jrbase.manager.CmdManager;

import java.util.HashMap;
import java.util.Map;

import static org.github.jrbase.dataType.ServerCmd.AUTH;
import static org.github.jrbase.handler.CommandParse.parseMessageToClientCmd;

public class CmdHandler {

    private static CmdHandler singleInstance;
    private CmdManager cmdManager = CmdManager.newSingleInstance();

    private static final String REPLY_OK = "OK";

    private static ScanServerAnnotationConfigure scanServerAnnotationConfigure = ScanServerAnnotationConfigure.newSingleInstance();

    /**
     * save session login status to HashMap<ChannelHandlerContext, RedisClientContext>
     */
    private final Map<ChannelHandlerContext, RedisClientContext> clientContext = new HashMap<>();

    private RedisConfigurationOption redisConfigurationOption;

    private CmdHandler() {
    }

    public static CmdHandler newSingleInstance(RedisConfigurationOption redisConfigurationOption) {
        if (singleInstance == null) {
            singleInstance = new CmdHandler();
            singleInstance.redisConfigurationOption = redisConfigurationOption;
        }
        return singleInstance;
    }

//    private void initHandlerMap() {
//        serverCmdHandlerMap.put(ECHO.getCmdName(), new EchoHandler());
//        serverCmdHandlerMap.put(PING.getCmdName(), new PingHandler());
//        serverCmdHandlerMap.put(COMMAND.getCmdName(), new CommandHandler());
////    }

    public void handleMsg(ChannelHandlerContext ctx, String message) {

        final ClientCmd clientCmd = parseMessage(message);

        if (checkRequirePassword(ctx, clientCmd)) {
            return;
        }

        if (clientCmd.getCmd().isEmpty()) {
            replyErrorToClient(ctx, "empty command");
        } else {
            final ServerCmdHandler serverCmdHandler = scanServerAnnotationConfigure.get(clientCmd.getCmd());
            if (serverCmdHandler != null) {
                //handle server command
                final String cmdHandlerResult = serverCmdHandler.handle(clientCmd);
                ctx.channel().writeAndFlush(cmdHandlerResult);
            } else {
                //handle data command
                clientCmd.setChannel(ctx.channel());
                clientCmd.setBackendProxy(new JraftKVDecorator(cmdManager.getRheaKVStore()));
                cmdManager.process(clientCmd);
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

    private void replyInfoToClient(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush("+" + msg + "\r\n");
    }

    private void replyErrorToClient(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush("-" + msg + "\r\n");
    }

}
