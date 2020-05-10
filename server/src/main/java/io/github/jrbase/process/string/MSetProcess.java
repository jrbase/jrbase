package io.github.jrbase.process.string;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.Database;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.database.StringRedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

@KeyCommand
public class MSetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.MSET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return !isWrongArgs(clientCmd);
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    // 1 key value, key value
    // 0  1    2    3    4
    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        addStringRedisValue(clientCmd.getDb(), clientCmd.getKey(), clientCmd.getArgs()[0]);
        final String[] args = clientCmd.getArgs();
        int successCount = 1;
        for (int i = 1; i < args.length; i = i + 2) {
            final String key = clientCmd.getArgs()[i];
            final String value = clientCmd.getArgs()[i + 1];
            addStringRedisValue(clientCmd.getDb(), key, value);
            successCount++;
        }
        return (":" + successCount + "\r\n");

    }

    private void addStringRedisValue(Database db, String key, String value) {
        final RedisValue redisValue = db.getOrDefault(key, new StringRedisValue());
        final StringRedisValue stringRedisValue = (StringRedisValue) redisValue;
        stringRedisValue.setValue(value);
        db.put(key, stringRedisValue);
    }

    private boolean isWrongArgs(ClientCmd clientCmd) {
        return clientCmd.getArgLength() <= 0 || clientCmd.getArgLength() % 2 == 0;
    }

}
