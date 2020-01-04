package org.github.jrbase.handler.connect;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.handler.ServerCmdHandler;

public class EchoHandler implements ServerCmdHandler {

    @Override
    public String handle(ClientCmd clientCmd) {
        StringBuilder result = new StringBuilder();
        final String echoMessage = clientCmd.getKey();
        if (echoMessage.isEmpty()) {
            result.append("-ERR wrong number of arguments for '").append(clientCmd.getCmd()).append("' command\r\n");
        } else {
            result.append("$").append(echoMessage.length()).append("\r\n").append(echoMessage).append("\r\n");
        }
        return result.toString();
    }
}
