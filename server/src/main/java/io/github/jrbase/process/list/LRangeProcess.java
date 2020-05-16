package io.github.jrbase.process.list;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.dataType.CommonMessage;
import io.github.jrbase.dataType.RedisDataType;
import io.github.jrbase.database.ListRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;
import org.apache.commons.lang.StringUtils;

import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

/**
 * Time complexity: O(S+N) where S is the distance of start offset from HEAD for small lists,
 * from nearest end (HEAD or TAIL) for large lists;
 * and N is the number of elements in the specified range.
 * <p>
 * Example: LRANGE mylist -100 100
 */
@KeyCommand
public class LRangeProcess implements CmdProcess {


    @Override
    public String getCmdName() {
        return Cmd.LRANGE.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        if (clientCmd.getArgLength() != 2) {
            return false;
        }
        if (!StringUtils.isNumeric(clientCmd.getArgs()[0]) || !StringUtils.isNumeric(clientCmd.getArgs()[1])) {
            clientCmd.setError(CommonMessage.REDIS_ERROR_INTEGER_OUT_RANGE);
            return false;
        }
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        synchronized (RedisDataType.LISTS) {
            final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
            if (redisValue == null) {
                return CommonMessage.REDIS_EMPTY_LIST;
            }
            if (!(redisValue instanceof ListRedisValue)) {
                return REDIS_ERROR_OPERATION_AGAINST;
            }
            final ListRedisValue listRedisValue = (ListRedisValue) redisValue;
            return listRedisValue.findRange(clientCmd.getArgs()[0], clientCmd.getArgs()[1]);
        }
    }

}
