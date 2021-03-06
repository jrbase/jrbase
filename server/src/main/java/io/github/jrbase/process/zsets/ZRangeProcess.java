package io.github.jrbase.process.zsets;

import io.github.jrbase.client.utils.skipList.SkipList;
import io.github.jrbase.common.datatype.Cmd;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.CommonMessage;
import io.github.jrbase.dataType.ScoreMember;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.database.ZSortRedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;
import org.apache.commons.lang.StringUtils;

import java.util.List;

import static io.github.jrbase.common.datatype.RedisDataType.SORTED_SETS;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

/**
 *
 */
@KeyCommand
public class ZRangeProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.ZRANGE.getCmdName();
    }

    private boolean withScore;

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        if (clientCmd.getArgLength() < 2 || clientCmd.getArgLength() > 3) {
            return false;
        }
        if (!StringUtils.isNumeric(clientCmd.getArgs()[0]) || !StringUtils.isNumeric(clientCmd.getArgs()[1])) {
            clientCmd.setError(CommonMessage.REDIS_ERROR_INTEGER_OUT_RANGE);
            return false;
        }
        if (clientCmd.getArgLength() == 3) {
            if (clientCmd.getArgs()[2].equals("withscores")) {
                this.withScore = true;
            } else {
                clientCmd.setError(CommonMessage.REDIS_ERROR_SYNTAX);
                return false;
            }
        }
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        synchronized (SORTED_SETS) {
            final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
            if (redisValue == null) {
                return CommonMessage.REDIS_EMPTY_LIST;
            }
            if (!(redisValue instanceof ZSortRedisValue)) {
                return REDIS_ERROR_OPERATION_AGAINST;
            }
            final ZSortRedisValue zSortRedisValue = (ZSortRedisValue) redisValue;
            List<SkipList<ScoreMember>.KVPair> range = zSortRedisValue.findRange(Integer.parseInt(clientCmd.getArgs()[0]), Integer.parseInt(clientCmd.getArgs()[1]));
            final StringBuilder result = new StringBuilder();
            result.append("*").append(range.size()).append("\r\n");
            for (SkipList<ScoreMember>.KVPair kvPair : range) {
                final String member = kvPair.key();
                final ScoreMember score = kvPair.value();
                if (this.withScore) {
                    result
                            .append("$").append(member.length()).append("\r\n")
                            .append(member).append("\r\n")
                            .append("$").append(String.valueOf(score).length()).append("\r\n")
                            .append(score).append("\r\n");
                } else {
                    result.append("$").append(member.length()).append("\r\n").append(member).append("\r\n");
                }
            }
            return result.toString();
        }
    }
}
