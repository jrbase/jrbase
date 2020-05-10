package io.github.jrbase.process.list;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.ListRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;
import io.github.jrbase.utils.list.ListNode;

@KeyCommand
public class LPushProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.LPUSH.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() >= 1;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {

        final RedisValue redisValue = clientCmd.getDb().getOrDefault(clientCmd.getKey(), new ListRedisValue());
        final ListRedisValue listRedisValue = (ListRedisValue) redisValue;
        for (String arg : clientCmd.getArgs()) {
            listRedisValue.addFirst(new ListNode(arg));
        }
        clientCmd.getDb().put(clientCmd.getKey(), redisValue);
        return (":" + listRedisValue.getSize() + "\r\n");

    }



}
