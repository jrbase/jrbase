package org.github.jrbase.process.zsets;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.database.RedisValue;
import org.github.jrbase.database.ZSortRedisValue;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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
        final RedisValue redisValue = clientCmd.getDb().getTable().get(clientCmd.getKey());
        if (redisValue == null) {
            final ZSortRedisValue zSortRedisValue = new ZSortRedisValue();
            getZSortRedisValue(clientCmd, zSortRedisValue);
            return ":" + zSortRedisValue.getValue().size() + "\r\n";
        }
        final ZSortRedisValue value = (ZSortRedisValue) redisValue;
        final int originSize = value.getValue().size();
        getZSortRedisValue(clientCmd, value);
        return ":" + (value.getValue().size() - originSize) + "\r\n";
    }

    @NotNull
    private ZSortRedisValue getZSortRedisValue(ClientCmd clientCmd, ZSortRedisValue zSortRedisValue) {
        final Map<String, Integer> keyValueMap = generateValueScoreValueMap(clientCmd.getArgs());
        for (String key : keyValueMap.keySet()) {
            zSortRedisValue.getValue().put(key, keyValueMap.get(key));
        }
        clientCmd.getDb().getTable().put(clientCmd.getKey(), zSortRedisValue);
        return zSortRedisValue;
    }

    private Map<String, Integer> generateValueScoreValueMap(String[] args) {
        Map<String, Integer> keyValueMap = new HashMap<>();
        for (int i = 0; i < args.length; i = i + 2) {
            keyValueMap.put(args[i + 1], Integer.valueOf(args[i]));
        }
        return keyValueMap;
    }

}
