package org.github.jrbase.handler.connect;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.handler.ServerCmdHandler;

public class PingHandler implements ServerCmdHandler {
    @Override
    public String handle(ClientCmd clientCmd) {
        StringBuilder result = new StringBuilder();
        final String pingMessage = clientCmd.getKey();
        if (pingMessage.isEmpty()) {
            result.append("+PONG\r\n");
        } else {
            result.append("$").append(pingMessage.length()).append("\r\n").append(pingMessage).append("\r\n");
        }
        return result.toString();
    }
}
