package io.github.jrbase.database;

import io.github.jrbase.dataType.RedisDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_INTEGER_OUT_RANGE;
import static io.github.jrbase.utils.sets.SetTools.getPopResult;
import static io.github.jrbase.utils.sets.SetTools.makeRandomSets;

public class SetRedisValue extends RedisValue {

    private final Set<String> sets = new HashSet<>();


    public SetRedisValue() {
        this.setType(RedisDataType.SETS);
    }

    public void add(String value) {
        sets.add(value);
    }

    public int getSize() {
        return sets.size();
    }

    public String pop(String[] args) {
        //2 error
        if (args.length > 1) {
            return "-ERR syntax error\r\n";
        } else if (args.length == 1) {  //3 have count argument
            return handleCountArgument(args[0]);
        } else {// 4 have no count argument
            return handleCountArgument("1");
        }
    }

    @NotNull
    private String handleCountArgument(String countStr) {
        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException ignore) {
            return REDIS_ERROR_INTEGER_OUT_RANGE;
        }
        if (count >= sets.size()) {
            return getPopResult(sets);
        }
        count = Math.min(count, sets.size());
        List<String> popRandomList = makeRandomSets(sets, count);
        return getPopResult(popRandomList);
    }
}

