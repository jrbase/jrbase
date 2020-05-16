package io.github.jrbase.handler.connect;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.handler.ServerCmdHandler;
import io.github.jrbase.handler.annotation.ServerCommand;

import static io.github.jrbase.dataType.ServerCmd.ECHO;

@ServerCommand
public class EchoHandler implements ServerCmdHandler {

    @Override
    public String handle(ClientCmd clientCmd) {
        StringBuilder result = new StringBuilder();
        final String echoMessage = clientCmd.getKey();
        if (echoMessage.isEmpty()) {
            result.append("-ERR wrong number of arguments for 'echo' command\r\n");
        } else {
            result.append("$").append(echoMessage.length()).append("\r\n").append(echoMessage).append("\r\n");
        }
        return result.toString();
    }

    @Override
    public String getCmdName() {
        return ECHO.getCmdName();
    }
}
