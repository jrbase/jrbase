package org.github.jrbase.process.hash;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.database.HashRedisValue;
import org.github.jrbase.database.RedisValue;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static org.github.jrbase.utils.Tools.checkArgs;

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

        final RedisValue redisValue = clientCmd.getDb().getTable().get(clientCmd.getKey());
        if (redisValue == null) {
            return REDIS_EMPTY_STRING;
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
