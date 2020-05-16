package io.github.jrbase.process.hash;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.HashRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER;
import static io.github.jrbase.dataType.RedisDataType.HASHES;

@KeyCommand
public class HLenProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.HLEN.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        synchronized (HASHES) {
            // hlen key
            final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
            if (redisValue == null) {
                return REDIS_ZORE_INTEGER;
            }
            if (!(redisValue instanceof HashRedisValue)) {
                return REDIS_ERROR_OPERATION_AGAINST;
            }
            return ":" + ((HashRedisValue) redisValue).getHash().size() + "\r\n";
        }
    }

}
