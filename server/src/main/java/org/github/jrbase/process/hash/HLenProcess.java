package org.github.jrbase.process.hash;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.database.HashRedisValue;
import org.github.jrbase.database.RedisValue;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER;

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
        checkKeyType();
        // hlen key
        final RedisValue redisValue = clientCmd.getDb().getTable().get(clientCmd.getKey());
        if (redisValue == null) {
            return REDIS_ZORE_INTEGER;
        }
        return ":" + ((HashRedisValue) redisValue).getHash().size() + "\r\n";
    }

    private void checkKeyType() {
        //TODO:
        // WRONGTYPE Operation against a key holding the wrong kind of value
    }

}
