package org.github.jrbase.handler.connect;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.handler.ServerCmdHandler;

public class CommandHandler implements ServerCmdHandler {
    @Override
    public String handle(ClientCmd clientCmd) {
        return "+OK\r\n";
    }
}