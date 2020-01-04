package org.github.jrbase.utils;

import org.jetbrains.annotations.NotNull;

import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER;

public class ToolsString {
    public static StringBuilder getLeftBuildUpArgsValue(@NotNull String[] args) {
        StringBuilder buildUpValue = new StringBuilder();
        for (int i = args.length - 1; i >= 0; i--) {
            buildUpValue.append(args[i]).append(REDIS_LIST_DELIMITER);
        }
        if (buildUpValue.length() != 0) {
            buildUpValue.deleteCharAt(buildUpValue.length() - 1);
        }
        return buildUpValue;
    }

    //lpush a b c  e f =>   c b a e f
    public static String getLeftBuildUpValue(@NotNull String[] args, @NotNull String[] originValueArr) {
        // origin value
        StringBuilder buildUpValue = new StringBuilder();
        for (int i = args.length - 1; i >= 0; i--) {
            buildUpValue.append(args[i]).append(REDIS_LIST_DELIMITER);
        }
        //set value
        for (String value : originValueArr) {
            buildUpValue.append(value).append(REDIS_LIST_DELIMITER);
        }
        if (buildUpValue.length() != 0) {
            buildUpValue.deleteCharAt(buildUpValue.length() - 1);
        }
        return buildUpValue.toString();
    }

    /**
     * dont add first value
     *
     * @param originValueArr get values from kV
     */
    public static String getLPopBuildUpValue(@NotNull String[] originValueArr) {
        StringBuilder buildUpValue = new StringBuilder();
        for (int i = 1; i < originValueArr.length; i++) {
            buildUpValue.append(originValueArr[i]).append(REDIS_LIST_DELIMITER);
        }
        if (buildUpValue.length() != 0) {
            buildUpValue.deleteCharAt(buildUpValue.length() - 1);
        }
        return buildUpValue.toString();
    }

    // rpush
    public static StringBuilder getRightBuildUpArgsValue(@NotNull String[] args) {
        StringBuilder buildUpValue = new StringBuilder();
        for (String arg : args) {
            buildUpValue.append(arg).append(REDIS_LIST_DELIMITER);
        }
        if (buildUpValue.length() != 0) {
            buildUpValue.deleteCharAt(buildUpValue.length() - 1);
        }
        return buildUpValue;
    }

    //lpush a b c , e f => e f a b c
    public static String getRightBuildUpValue(@NotNull String[] args, @NotNull String[] originValueArr) {
        // origin value +
        StringBuilder buildUpValue = new StringBuilder();
        //set value
        for (String value : originValueArr) {
            buildUpValue.append(value).append(REDIS_LIST_DELIMITER);
        }
        for (String arg : args) {
            buildUpValue.append(arg).append(REDIS_LIST_DELIMITER);
        }
        if (buildUpValue.length() != 0) {
            buildUpValue.deleteCharAt(buildUpValue.length() - 1);
        }
        return buildUpValue.toString();
    }

    /**
     * dont add last value
     *
     * @param originValueArr get values from kV
     */
    public static String getRPopBuildUpValue(@NotNull String[] originValueArr) {
        StringBuilder buildUpValue = new StringBuilder();
        for (int i = 0; i < originValueArr.length - 1; i++) {
            buildUpValue.append(originValueArr[i]).append(REDIS_LIST_DELIMITER);
        }
        if (buildUpValue.length() != 0) {
            buildUpValue.deleteCharAt(buildUpValue.length() - 1);
        }
        return buildUpValue.toString();
    }

    public static String toRedisListDelimiter(String str) {
        StringBuilder result = new StringBuilder();
        final String[] split = str.split(",");
        for (String s : split) {
            result.append(s).append(REDIS_LIST_DELIMITER);
        }
        if (result.length() == 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }
}
