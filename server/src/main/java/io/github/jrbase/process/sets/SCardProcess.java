package io.github.jrbase.process.sets;

import io.github.jrbase.common.datatype.Cmd;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.database.SetRedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_LIST;

/**
 * SCARD key
 * Time complexity: O(1)
 * Returns the set cardinality (number of elements) of the set stored at key.
 */
@KeyCommand
public class SCardProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return Cmd.SCARD.getCmdName();
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
        final SetRedisValue setRedisValue = (SetRedisValue) redisValue;
        return ":" + setRedisValue.getSize() + "\r\n";
    }
}
