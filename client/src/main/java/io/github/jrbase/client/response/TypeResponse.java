package io.github.jrbase.client.response;

/**
 * Redis response type
 *
 * 1 +  Simple Strings
 * "+OK\r\n"
 * 2 - Errors
 * "-Error message\r\n"
 * 3 : Integers
 * ":0\r\n"
 * 4 $  Bulk Strings
 * "$6\r\nfoobar\r\n"
 * "$0\r\n\r\n"
 * "$-1\r\n"
 *
 * 5 * Arrays
 * "*0\r\n"
 * "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n"
 *
 * "*3\r\n:1\r\n:2\r\n:3\r\n"
 *
 * *5\r\n
 * :1\r\n
 * :2\r\n
 * :3\r\n
 * :4\r\n
 * $6\r\n
 *
 * foobar\r\n
 *
 * "*-1\r\n"
 *
 * Strategy patten
 */
public interface TypeResponse {
    /**
     * parse response string
     * @param command input command
     */
    void handle(String command);
}
