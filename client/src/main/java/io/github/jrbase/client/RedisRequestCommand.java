package io.github.jrbase.client;

public class RedisRequestCommand {

    // 使用空格分割组成数组字符串
    // parse command
    public static String toRedisCommand(String command) {
        String[] items = getItems(command);
        return mergeItems(items);
    }

    public static String[] getItems(String command) {
        return command.trim().split("( )+");
    }

    /**
     * 组合　command
     *
     * @param items set key value =>
     * @return *3\r\n$3\r\nset\r\n$3\r\nkey\r\n$5\r\nvalue\r\n
     */
    private static String mergeItems(String[] items) {
        StringBuilder result = new StringBuilder();
        result.append("*").append(items.length).append("\r\n");
        for (String item : items) {
            result.append("$").append(item.length()).append("\r\n").append(item).append("\r\n");
        }
        return result.toString();
    }
}
