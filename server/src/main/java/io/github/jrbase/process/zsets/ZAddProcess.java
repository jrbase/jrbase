package io.github.jrbase.process.zsets;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.database.ZSortRedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import java.util.HashMap;
import java.util.Map;

import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;
import static io.github.jrbase.dataType.RedisDataType.SORTED_SETS;

/**
 * ZADD key [NX|XX] [CH] [INCR] score member [score member ...]
 */

@KeyCommand
public class ZAddProcess implements CmdProcess {
    // TODO: parse [NX|XX] [CH] [INCR]
    @Override
    public String getCmdName() {
        return Cmd.ZADD.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() >= 2;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        synchronized (SORTED_SETS) {
            final RedisValue redisValue = clientCmd.getDb().getOrDefault(clientCmd.getKey(), new ZSortRedisValue());
            if (!(redisValue instanceof ZSortRedisValue)) {
                return REDIS_ERROR_OPERATION_AGAINST;
            }
            final ZSortRedisValue zSortRedisValue = (ZSortRedisValue) redisValue;
            final int originSize = zSortRedisValue.getSize();
            updateZSortRedisValue(clientCmd, zSortRedisValue);
            clientCmd.getDb().put(clientCmd.getKey(), zSortRedisValue);
            return ":" + (zSortRedisValue.getSize() - originSize) + "\r\n";
        }
    }

    private void updateZSortRedisValue(ClientCmd clientCmd, ZSortRedisValue zSortRedisValue) {
        final Map<String, Integer> keyValueMap = generateValueScoreValueMap(clientCmd.getArgs());
        for (String key : keyValueMap.keySet()) {
            zSortRedisValue.put(key, keyValueMap.get(key));
        }
    }

    private Map<String, Integer> generateValueScoreValueMap(String[] args) {
        Map<String, Integer> keyValueMap = new HashMap<>();
        for (int i = 0; i < args.length; i = i + 2) {
            keyValueMap.put(args[i + 1], Integer.valueOf(args[i]));
        }
        return keyValueMap;
    }

}
