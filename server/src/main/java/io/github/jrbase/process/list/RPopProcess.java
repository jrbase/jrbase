package io.github.jrbase.process.list;

import io.github.jrbase.client.utils.list.ListNode;
import io.github.jrbase.common.datatype.Cmd;
import io.github.jrbase.common.datatype.RedisDataType;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.database.ListRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.dataType.CommonMessage.*;

/**
 * Time complexity: O(1)
 * RPUSH mylist
 */
@KeyCommand
public class RPopProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.RPOP.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    // a b c d
    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        synchronized (RedisDataType.LISTS) {
            final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
            if (redisValue == null) {
                return REDIS_EMPTY_LIST;
            }

            if (!(redisValue instanceof ListRedisValue)) {
                return REDIS_ERROR_OPERATION_AGAINST;
            }
            final ListRedisValue listRedisValue = (ListRedisValue) redisValue;
            ListNode rPopValue = listRedisValue.popLast();
            if (rPopValue == null) {
                return REDIS_EMPTY_STRING;
            } else {
                return ("$" + rPopValue.getValue().length() + "\r\n" + rPopValue.getValue() + "\r\n");
            }
        }
    }

}
