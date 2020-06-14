package io.github.jrbase.process.hash;

import io.github.jrbase.client.utils.ToolsKeyValue;
import io.github.jrbase.common.datatype.Cmd;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.database.HashRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import java.util.Map;

import static io.github.jrbase.common.datatype.RedisDataType.HASHES;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

@KeyCommand
public class HSetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.HSET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return ToolsKeyValue.keyValueEventNumber(clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {

        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        // hset key field1 value1 field2 value2
        synchronized (HASHES) {
            final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
            if (redisValue == null) {
                final HashRedisValue addHash = new HashRedisValue();
                getSuccessCountUpdate(clientCmd, addHash);
                clientCmd.getDb().put(clientCmd.getKey(), addHash);
                return ":" + addHash.getHash().size() + "\r\n";
            }
            if (!(redisValue instanceof HashRedisValue)) {
                return REDIS_ERROR_OPERATION_AGAINST;
            }
            final Map<String, String> hash = ((HashRedisValue) redisValue).getHash();
            final int originSize = hash.size();
            getSuccessCountUpdate(clientCmd, redisValue);
            final int currentSize = ((HashRedisValue) redisValue).getHash().size();
            final int updateSize = currentSize - originSize;
            return ":" + updateSize + "\r\n";
        }
    }

    private void getSuccessCountUpdate(ClientCmd clientCmd, RedisValue addHash) {
        final Map<String, String> keyValueMap = ToolsKeyValue.generateKeyValueMap(clientCmd.getArgs());
        for (String field : keyValueMap.keySet()) {
            ((HashRedisValue) addHash).getHash().put(field, keyValueMap.get(field));
        }
    }

}
