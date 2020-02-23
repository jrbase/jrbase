package org.github.jrbase.handler.connect;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.handler.ServerCmdHandler;
import org.github.jrbase.handler.annotation.ServerCommand;

import static org.github.jrbase.dataType.ServerCmd.COMMAND;

@ServerCommand
public class CommandHandler implements ServerCmdHandler {
    @Override
    public String handle(ClientCmd clientCmd) {
        return "+OK\r\n";
    }

    @Override
    public String getCmdName() {
        return COMMAND.getCmdName();
    }
}
