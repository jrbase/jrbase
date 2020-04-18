package org.github.jrbase.process.keys;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.database.RedisValue;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

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
        final RedisValue redisValue = clientCmd.getDb().getTable().get(clientCmd.getKey());
        if (redisValue == null) {
            return "$" + RedisDataType.NONE.getName().length() + "\r\n" + RedisDataType.NONE.getName() + "\r\n";
        }
        return "$" + redisValue.getType().getName().length() + "\r\n" + redisValue.getType().getName() + "\r\n";
    }
}
