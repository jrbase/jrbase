package io.github.jrbase.process.keys;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.dataType.RedisDataType;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

@KeyCommand
public class TypeProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.TYPE.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() == 0;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        //set key currentTime + args[0]ms
        final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
        if (redisValue == null) {
            return "$" + RedisDataType.NONE.getName().length() + "\r\n" + RedisDataType.NONE.getName() + "\r\n";
        }
        return "$" + redisValue.getType().getName().length() + "\r\n" + redisValue.getType().getName() + "\r\n";
    }
}
