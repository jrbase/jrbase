package io.github.jrbase.process.sets;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.database.SetRedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

/**
 * SADD key member [member ...]
 * Time complexity: O(1) for each element added, so O(N) to add N elements when the command is called with multiple arguments.
 */
@KeyCommand
public class SAddProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.SADD.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() > 0;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        final RedisValue redisValue = clientCmd.getDb().getOrDefault(clientCmd.getKey(), new SetRedisValue());
        final SetRedisValue setRedisValue = (SetRedisValue) redisValue;
        final int originSize = setRedisValue.getSize();
        for (String arg : clientCmd.getArgs()) {
            setRedisValue.add(arg);
        }
        clientCmd.getDb().put(clientCmd.getKey(), setRedisValue);
        return ":" + (setRedisValue.getSize() - originSize) + "\r\n";
    }

}
