package org.github.jrbase.process.string;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.database.RedisValue;
import org.github.jrbase.database.StringRedisValue;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import static org.github.jrbase.dataType.CommonMessage.REDIS_ONE_INTEGER;
import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER;
import static org.github.jrbase.utils.Tools.checkArgs;

@KeyCommand
public class SetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.SET.getCmdName();
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
        synchronized (this) {
            final RedisValue redisValue = clientCmd.getDb().getTable().get(clientCmd.getKey());
            if (redisValue == null) {
                final StringRedisValue addStringRedisValue = new StringRedisValue();
                addStringRedisValue.setValue(clientCmd.getArgs()[0]);
                clientCmd.getDb().getTable().put(clientCmd.getKey(), addStringRedisValue);
                return REDIS_ONE_INTEGER;
            }
            ((StringRedisValue) redisValue).setValue(clientCmd.getArgs()[0]);
            clientCmd.getDb().getTable().put(clientCmd.getKey(), redisValue);
            return REDIS_ZORE_INTEGER;
        }
    }

}
