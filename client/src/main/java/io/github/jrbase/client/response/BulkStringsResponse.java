package io.github.jrbase.client.response;

/**
 * Bulk Strings
 */
public class BulkStringsResponse implements TypeResponse {
    @Override
    public String handle(String command) {
        // "$6\r\nfoobar\r\n"
        // "$0\r\n"
        // "$-1\r\n"
        if (command.startsWith("$-1")) {
            return ("(nil)");
        } else {
            String[] split = command.split("\r\n");
            return ("\"" + split[1] + "\"");
        }
    }


}
