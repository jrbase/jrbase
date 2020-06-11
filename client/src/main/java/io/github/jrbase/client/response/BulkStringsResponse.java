package io.github.jrbase.client.response;

/**
 * Bulk Strings
 */
public class BulkStringsResponse implements TypeResponse {
    @Override
    public void handle(String command) {
        // "$6\r\nfoobar\r\n"
        // "$0\r\n"
        // "$-1\r\n"
        if (command.startsWith("$-1")) {
            System.out.println("(nil)");
        } else {
            String[] split = command.split("\r\n");
            System.out.println(split[1]);
        }
    }


}
