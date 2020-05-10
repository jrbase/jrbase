package io.github.jrbase.process.keys;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.dataType.CommonMessage.REDIS_ONE_INTEGER;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER;

@KeyCommand
public class ExpireProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.EXPIRE.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() == 1;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        //set key currentTime + args[0]ms
        final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
        if (redisValue == null) {
            return REDIS_ZORE_INTEGER;
        }
        final String second = clientCmd.getArgs()[0];
        final int s = Integer.parseInt(second);
        final int ms = s * 1000;
        final long valueMs = System.currentTimeMillis() + ms;
        redisValue.setExpire(valueMs);
        clientCmd.getDb().put(clientCmd.getKey(), redisValue);
        return REDIS_ONE_INTEGER;

    }
}
