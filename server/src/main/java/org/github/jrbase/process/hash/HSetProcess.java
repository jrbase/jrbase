package org.github.jrbase.process.hash;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.database.HashRedisValue;
import org.github.jrbase.database.RedisValue;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import java.util.Map;

import static org.github.jrbase.utils.ToolsKeyValue.generateKeyValueMap;
import static org.github.jrbase.utils.ToolsKeyValue.keyValueEventNumber;

@KeyCommand
public class HSetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.HSET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return keyValueEventNumber(clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {

        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        // hset key field1 value1 field2 value2
        final RedisValue redisValue = clientCmd.getDb().getTable().get(clientCmd.getKey());
        if (redisValue == null) {
            final HashRedisValue addHash = new HashRedisValue();
            getSuccessCountUpdate(clientCmd, addHash);
            clientCmd.getDb().getTable().put(clientCmd.getKey(), addHash);
            return ":" + addHash.getHash().size() + "\r\n";
        }
        final Map<String, String> hash = ((HashRedisValue) redisValue).getHash();
        final int originSize = hash.size();
        getSuccessCountUpdate(clientCmd, redisValue);
        final int currentSize = ((HashRedisValue) redisValue).getHash().size();
        final int updateSize = currentSize - originSize;
        return ":" + updateSize + "\r\n";
    }

    private void getSuccessCountUpdate(ClientCmd clientCmd, RedisValue addHash) {
        final Map<String, String> keyValueMap = generateKeyValueMap(clientCmd.getArgs());
        for (String field : keyValueMap.keySet()) {
            ((HashRedisValue) addHash).getHash().put(field, keyValueMap.get(field));
        }
    }

}
