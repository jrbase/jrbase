package io.github.jrbase.client.response;

/**
 * * Arrays
 * "*0\r\n"
 * 0      1     2     3     4
 * "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n"
 * <p>
 * "*3\r\n:1\r\n:2\r\n:3\r\n"
 * <p>
 * *5\r\n
 * :1\r\n
 * :2\r\n
 * :3\r\n
 * :4\r\n
 * $6\r\n
 * <p>
 * "*-1\r\n"
 */
public class ArraysResponse implements TypeResponse {
    @Override
    public String handle(String command) {
        String[] split = command.split("\r\n");
        int j = 1;
        int i = 1;
        StringBuilder result = new StringBuilder();
        while (i < split.length) {
            if (split[i].startsWith("$-1")) {
                result.append(j).append(") ").append("(nil)");
                i = i + 1;
            } else {
                result.append(j).append(") ").append("\"").append(split[i + 1]).append("\"");
                i = i + 2;
            }
            j++;
        }
        return result.toString();
    }

}
