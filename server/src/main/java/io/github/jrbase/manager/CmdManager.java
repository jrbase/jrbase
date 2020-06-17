package io.github.jrbase.manager;

import io.github.jrbase.client.utils.Tools;
import io.github.jrbase.common.datatype.Cmd;
import io.github.jrbase.common.datatype.RedisDataType;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.factory.CommandAbstractFactory;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.ScanAnnotationConfigure;
import io.netty.channel.Channel;
import org.apache.commons.lang.StringUtils;

import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;


public class CmdManager {


    private static final ScanAnnotationConfigure<CmdProcess> scanAnnotationConfigure = CommandAbstractFactory.newProcessAnnotationConfigure();
    private static CmdManager singleInstance;

    private CmdManager() {
    }

    public static CmdManager newSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new CmdManager();
        }
        return singleInstance;
    }
    // Template Pattern
    public void process(ClientCmd clientCmd) {
        // Strategy patten
        CmdProcess cmdProcess = clientCmdToCmdProcess(clientCmd);
        if (cmdProcess == null) {
            cmdProcess = scanAnnotationConfigure.get(Cmd.OTHER.getCmdName());
        }
        // check key
        if (!Tools.isCorrectKey(clientCmd.getKey())) {
            sendWrongArgumentMessage(clientCmd);
            return;
        }
        // check arguments
        if (!cmdProcess.isCorrectArguments(clientCmd)) {
            if (StringUtils.isNotBlank(clientCmd.getError())) {
                sendCustomError(clientCmd);
            } else {
                sendWrongArgumentMessage(clientCmd);
            }
            return;
        }
        // check type
        if (checkWrongType(clientCmd)) {
            sendWrongTypeMessage(clientCmd);
            return;
        }
        final String message = cmdProcess.process(clientCmd);
        clientCmd.getChannel().writeAndFlush(message);

    }

    private boolean checkWrongType(ClientCmd clientCmd) {
        final Cmd cmd = Cmd.get(clientCmd.getCmd());
        if (cmd.getType().equals(RedisDataType.KEY)) {
            return false;
        }
        final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
        return redisValue != null && !cmd.getType().equals(redisValue.getType());
    }

    private void sendCustomError(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getChannel();
        channel.writeAndFlush(clientCmd.getError());
    }

    private static void sendWrongArgumentMessage(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getChannel();
        channel.writeAndFlush("-ERR wrong number of arguments for '" + clientCmd.getCmd() + "' command\r\n");
    }

    private static void sendWrongTypeMessage(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getChannel();
        channel.writeAndFlush(REDIS_ERROR_OPERATION_AGAINST);
    }

    CmdProcess clientCmdToCmdProcess(ClientCmd clientCmd) {
        return scanAnnotationConfigure.get(clientCmd.getCmd());
    }

}
