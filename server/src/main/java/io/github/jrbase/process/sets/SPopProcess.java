package io.github.jrbase.process.sets;

import io.github.jrbase.common.datatype.Cmd;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.database.SetRedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_LIST;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

/**
 * spop key [count]
 * Removes and returns one or more random elements from the set value store at key.
 */
@KeyCommand
public class SPopProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return Cmd.SPOP.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
        if (redisValue == null) {
            return REDIS_EMPTY_LIST;
        }
        if (!(redisValue instanceof SetRedisValue)) {
            return REDIS_ERROR_OPERATION_AGAINST;
        }
        final SetRedisValue setRedisValue = (SetRedisValue) redisValue;
        return setRedisValue.pop(clientCmd.getArgs());
    }

}
