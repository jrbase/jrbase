package org.github.jrbase.process.string;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.database.RedisValue;
import org.github.jrbase.database.StringRedisValue;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static org.github.jrbase.utils.Tools.checkArgs;

@KeyCommand
public class GetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.GET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return checkArgs(0, clientCmd.getArgLength());
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
        final long expireMs = redisValue.getExpire();
        if (expireMs != 0 && System.currentTimeMillis() > expireMs) {// expire true
            // delete key
            clientCmd.getDb().getTable().put(clientCmd.getKey(), null);
            return REDIS_EMPTY_STRING;
        }

        final String value = ((StringRedisValue) redisValue).getValue();
        final int length = value.length();
        return "$" + length + "\r\n" + value + "\r\n";
    }

}
