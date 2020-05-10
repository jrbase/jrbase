package io.github.jrbase.utils.sets;

import org.apache.commons.lang.math.RandomUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SetTools {
    @NotNull
    public static String getPopResult(final List<String> getValueArr) {
        StringBuilder sPopResult = new StringBuilder();
        sPopResult.append("*").append(getValueArr.size()).append("\r\n");
        for (String s : getValueArr) {
            sPopResult.append("$").append(s.length()).append("\r\n").append(s).append("\r\n");
        }
        return sPopResult.toString();
    }

    @NotNull
    public static String getPopResult(final Set<String> getValueArr) {
        StringBuilder sPopResult = new StringBuilder();
        sPopResult.append("*").append(getValueArr.size()).append("\r\n");
        for (String s : getValueArr) {
            sPopResult.append("$").append(s.length()).append("\r\n").append(s).append("\r\n");
        }
        return sPopResult.toString();
    }

    public static List<String> makeRandomSets(Set<String> originSets, int count) {
        count = Math.min(count, originSets.size());
        List<String> result = new ArrayList<>(count);
        final String[] arr = originSets.toArray(new String[]{});
        while (result.size() < count) {
            final int randomNum = RandomUtils.nextInt(originSets.size());
            final String popValue = arr[randomNum];
            result.add(popValue);
            originSets.remove(popValue);
        }
        return result;
    }

}
