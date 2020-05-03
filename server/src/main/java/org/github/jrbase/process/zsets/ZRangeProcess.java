package org.github.jrbase.process.zsets;

import org.apache.commons.lang.StringUtils;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.database.RedisValue;
import org.github.jrbase.database.ZSortRedisValue;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;
import org.github.jrbase.utils.skipList.KVPair;

import java.util.List;

import static org.github.jrbase.dataType.CommonMessage.*;

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
            clientCmd.setError(REDIS_ERROR_INTEGER_OUT_RANGE);
            return false;
        }
        if (clientCmd.getArgLength() == 3) {
            if (clientCmd.getArgs()[2].equals("withscores")) {
                this.withScore = true;
            } else {
                clientCmd.setError(REDIS_ERROR_SYNTAX);
                return false;
            }
        }
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        final RedisValue redisValue = clientCmd.getDb().getTable().get(clientCmd.getKey());
        if (redisValue == null) {
            return REDIS_EMPTY_LIST;
        }
        final ZSortRedisValue zSortRedisValue = (ZSortRedisValue) redisValue;
        final List<KVPair> range = zSortRedisValue.findRange(Integer.parseInt(clientCmd.getArgs()[0]), Integer.parseInt(clientCmd.getArgs()[1]));
        StringBuilder result = new StringBuilder();
        result.append("*").append(range.size()).append("\r\n");
        for (KVPair kvPair : range) {
            final String member = kvPair.key().getMember();
            final int score = kvPair.key().getScore();
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
