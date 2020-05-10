package io.github.jrbase.process.string;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.database.StringRedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

@KeyCommand
public class MGetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.MGET.getCmdName();
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

        // key is first arg
        List<String> result = new ArrayList<>();
        RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());

        if (redisValue == null) {
            result.add("-1");
        } else {
            if (!(redisValue instanceof StringRedisValue)) {
                return REDIS_ERROR_OPERATION_AGAINST;
            }
            String value = ((StringRedisValue) redisValue).getValue();
            result.add(value);
        }
        for (String key : clientCmd.getArgs()) {
            redisValue = clientCmd.getDb().get(key);
            if (redisValue == null) {
                result.add("-1");
            } else {
                if (!(redisValue instanceof StringRedisValue)) {
                    return REDIS_ERROR_OPERATION_AGAINST;
                }
                String value = ((StringRedisValue) redisValue).getValue();
                result.add(value);
            }
        }

        return mgetResult(result);
    }

    @NotNull
    public static String mgetResult(final List<String> getValueArr) {
        StringBuilder sPopResult = new StringBuilder();
        sPopResult.append("*").append(getValueArr.size()).append("\r\n");
        for (String s : getValueArr) {
            if (s.equals("-1")) {
                sPopResult.append("$").append(-1).append("\r\n");
            } else {
                sPopResult.append("$").append(s.length()).append("\r\n").append(s).append("\r\n");
            }

        }
        return sPopResult.toString();
    }

}
