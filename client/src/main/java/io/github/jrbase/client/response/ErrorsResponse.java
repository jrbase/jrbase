package io.github.jrbase.client.response;

public class ErrorsResponse implements TypeResponse {
    @Override
    public void handle(String command) {
        String msg = command.substring(1, command.length() - 2);
        System.out.println("(error) " + msg);
    }
}
