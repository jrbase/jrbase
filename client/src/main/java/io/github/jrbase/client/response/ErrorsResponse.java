package io.github.jrbase.client.response;

public class ErrorsResponse implements TypeResponse {
    @Override
    public String handle(String command) {
        String msg = command.substring(1, command.length() - 2);
        return ("(error) " + msg);
    }
}
