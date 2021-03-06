package io.github.jrbase.client.response;

/**
 *  : Integers
 *  hset key 1 2
 *  (integer) 1
 */
public class IntegersResponse implements TypeResponse {
    @Override
    public String handle(String command) {
        // ":0\r\n" => (integer) 0
        String msg = command.substring(1, command.length() - 2);
        return ("(integer) " + msg);
    }
}
