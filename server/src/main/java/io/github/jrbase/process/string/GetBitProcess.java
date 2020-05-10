package io.github.jrbase.process.string;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.ByteRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;
import io.github.jrbase.utils.Tools;

import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;
import static io.github.jrbase.utils.Tools.checkArgs;

@KeyCommand
public class GetBitProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.GETBIT.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return checkArgs(1, clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {

        final String[] args = clientCmd.getArgs();
        int positionInt;
        try {
            positionInt = Integer.parseInt(args[0]);
            if (positionInt > SetBitProcess.MAX_BIT || positionInt < 0) {
                return ("-ERR bit offset is not an integer or out of range\r\n");
            }
        } catch (NumberFormatException ignore) {
            return ("-ERR bit offset is not an integer or out of range\r\n");
        }

        final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
        if (redisValue == null) {
            return REDIS_EMPTY_STRING;
        }
        if (!(redisValue instanceof ByteRedisValue)) {
            return REDIS_ERROR_OPERATION_AGAINST;
        }
        final ByteRedisValue byteRedisValue = (ByteRedisValue) redisValue;

        if (null == byteRedisValue.getValue()) {
            return REDIS_EMPTY_STRING;
        }
        StringBuilder result = new StringBuilder();
        final byte[] bytes = ((ByteRedisValue) redisValue).getValue();
        final int bit = Tools.getBit(positionInt, bytes);
        if (bit == -1) {
            result.append("-ERR bit offset is not an integer or out of range\r\n");
        } else {
            result.append(":").append(bit).append("\r\n");
        }
        return result.toString();
    }


}
