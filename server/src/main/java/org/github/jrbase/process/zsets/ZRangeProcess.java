package org.github.jrbase.process.zsets;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.database.RedisValue;
import org.github.jrbase.database.ZSortRedisValue;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_LIST;

@KeyCommand
public class ZRangeProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.ZRANGE.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() == 2;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        final RedisValue redisValue = clientCmd.getDb().getTable().get(clientCmd.getKey());
        if (redisValue == null) {
            return REDIS_EMPTY_LIST;
        }
        final ZSortRedisValue value = (ZSortRedisValue) redisValue;
//        final TreeMap<String, Integer> rangeValue = value.getHashValue();
//        rangeValue.get()

        return "";
    }
}
