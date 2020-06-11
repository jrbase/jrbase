package io.github.jrbase.client.response;

public class SimpleStringsResponse implements TypeResponse {
    @Override
    public void handle(String command) {
        //+OK\r\n => OK
        String msg = getMsg(command);
        System.out.println(msg);
    }

    private String getMsg(String command) {
        return command.substring(1, command.length() - 2);
    }
}
