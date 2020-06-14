package io.github.jrbase.process.hash;

import io.github.jrbase.common.datatype.Cmd;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.database.HashRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.client.utils.Tools.checkArgs;
import static io.github.jrbase.common.datatype.RedisDataType.HASHES;
import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

@KeyCommand
public class HGetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.HGET.getCmdName();
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
        synchronized (HASHES) {
            final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
            if (redisValue == null) {
                return REDIS_EMPTY_STRING;
            }
            if (!(redisValue instanceof HashRedisValue)) {
                return REDIS_ERROR_OPERATION_AGAINST;
            }
            StringBuilder result = new StringBuilder();
            final String value = ((HashRedisValue) redisValue).getHash().get(clientCmd.getArgs()[0]);
            if (value == null) {
                return REDIS_EMPTY_STRING;
            }
            final int length = value.length();
            result.append("$").append(length).append("\r\n").append(value).append("\r\n");
            return result.toString();
        }
    }

}
