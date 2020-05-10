package io.github.jrbase.process.string;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.database.StringRedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;
import io.github.jrbase.utils.Tools;

import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

@KeyCommand
public class GetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.GET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return Tools.checkArgs(0, clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {

        final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
        if (redisValue == null) {
            return REDIS_EMPTY_STRING;
        }
        final long expireMs = redisValue.getExpire();
        if (expireMs != 0 && System.currentTimeMillis() > expireMs) {// expire true
            // delete key
            clientCmd.getDb().put(clientCmd.getKey(), null);
            return REDIS_EMPTY_STRING;
        }
        if (!(redisValue instanceof StringRedisValue)) {
            return REDIS_ERROR_OPERATION_AGAINST;
        }
        final String value = ((StringRedisValue) redisValue).getValue();
        final int length = value.length();
        return "$" + length + "\r\n" + value + "\r\n";
    }

}
