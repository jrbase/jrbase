package io.github.jrbase.handler;

import io.github.jrbase.common.config.RedisConfigurationOption;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.RedisClientContext;
import io.github.jrbase.database.Database;
import io.github.jrbase.factory.CommandAbstractFactory;
import io.github.jrbase.handler.pubsub.RedisChannel;
import io.github.jrbase.manager.CmdManager;
import io.github.jrbase.process.annotation.ScanAnnotationConfigure;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.jrbase.dataType.ServerCmd.AUTH;

public class CmdHandler {
    private static final List<Database> Databases = new ArrayList<>(16);

    private static CmdHandler singleInstance;
    private final CmdManager cmdManager = CmdManager.newSingleInstance();

    public Database getDefaultDB() {
        return Databases.get(0);
    }

    private final int defaultDB = 0;

    private static final String REPLY_OK = "OK";

    private static final ScanAnnotationConfigure<ServerCmdHandler> scanServerAnnotationConfigure = CommandAbstractFactory.newHandlerAnnotation();

    /**
     * save session login status to HashMap<ChannelHandlerContext, RedisClientContext>
     */
    private final Map<ChannelHandlerContext, RedisClientContext> clientContext = new ConcurrentHashMap<>();

    private RedisConfigurationOption redisConfigurationOption;

    private CmdHandler() {
        for (int i = 0; i < 16; i++) {
            Databases.add(new Database(i));
        }
    }

    public static CmdHandler newSingleInstance(RedisConfigurationOption redisConfigurationOption) {
        if (singleInstance == null) {
            singleInstance = new CmdHandler();
            singleInstance.redisConfigurationOption = redisConfigurationOption;
        }
        return singleInstance;
    }

    /**
     * handle raw string to ClientCmd
     *
     * @param ctx     socket handler
     * @param message raw string from redis-cli
     */
    public void handleMsg(ChannelHandlerContext ctx, String message) {

        final ClientCmd clientCmd = parseMessage(message);
        final Database database = Databases.get(defaultDB);
        clientCmd.setDb(database);
        if (checkRequirePassword(ctx, clientCmd)) {
            return;
        }
        if (clientCmd.getCmd().isEmpty()) {
            replyErrorToClient(ctx, "empty command");
            return;
        }
        // Strategy patten
        final ServerCmdHandler serverCmdHandler = scanServerAnnotationConfigure.get(clientCmd.getCmd());
        if (serverCmdHandler != null) {
            //handle server command
            final String cmdHandlerResult = serverCmdHandler.handle(clientCmd);
            ctx.channel().writeAndFlush(cmdHandlerResult);
        } else {
            //handle data command
            clientCmd.setChannel(ctx.channel());
            cmdManager.process(clientCmd);
        }

    }

    // handle Context then check password
    private boolean checkRequirePassword(final ChannelHandlerContext ctx, final ClientCmd clientCmd) {
        RedisClientContext redisClientContext = clientContext.get(ctx);

        if (redisClientContext == null) {
            redisClientContext = new RedisClientContext();
            redisClientContext.setRedisClient(ctx);
            clientContext.put(ctx, redisClientContext);
        }
        clientCmd.setRedisClientContext(redisClientContext);
        if (StringUtils.isNotEmpty(redisConfigurationOption.getRequirePass()) && isLogin(redisClientContext)) {
            handleAuth(ctx, clientCmd, redisClientContext);
            return true;
        }
        return false;
    }

    private boolean isLogin(RedisClientContext redisClientContext) {
        return redisClientContext.isLogin();
    }

    private void handleAuth(ChannelHandlerContext ctx, ClientCmd clientCmd, RedisClientContext redisClientContext) {
        if (AUTH.getCmdName().equals(clientCmd.getCmd())) {
            if (authPassword(clientCmd)) {
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

    // TODO: handle multi command in a message
    // redis-benchmark -a 123456 -t get -n 1

    /**
     * *2
     * $4
     * AUTH
     * $6
     * 123456
     * *2
     * $3
     * GET
     * $16
     * key:__rand_int__
     */
    ClientCmd parseMessage(String message) {
        // *3\r\n$3\r\nset\r\n$3\r\nkey$5\r\nvalue\r\n).split("\r\n")
        final String[] redisClientCmdArr = message.split("\r\n");
        return CommandParse.parseMessageToClientCmd(redisClientCmdArr);
    }

    private void replyInfoToClient(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush("+" + msg + "\r\n");
    }

    private void replyErrorToClient(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush("-" + msg + "\r\n");
    }

    public void removeContext(ChannelHandlerContext ctx) {
        final RedisClientContext redisClientContext = clientContext.get(ctx);
        RedisChannel.unSubscribe(redisClientContext);
        clientContext.remove(ctx);
    }
}
