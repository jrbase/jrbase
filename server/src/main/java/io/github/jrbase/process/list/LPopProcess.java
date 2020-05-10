package io.github.jrbase.process.list;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.ListRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;
import io.github.jrbase.utils.list.ListNode;

import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;

@KeyCommand
public class LPopProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.LPOP.getCmdName();
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
        final RedisValue redisValue = clientCmd.getDb().getOrDefault(clientCmd.getKey(), new ListRedisValue());
        final ListRedisValue listRedisValue = (ListRedisValue) redisValue;
        ListNode rPopValue = listRedisValue.popFirst();
        if (rPopValue == null) {
            return REDIS_EMPTY_STRING;
        } else {
            return ("$" + rPopValue.getValue().length() + "\r\n" + rPopValue.getValue() + "\r\n");
        }
    }

}
