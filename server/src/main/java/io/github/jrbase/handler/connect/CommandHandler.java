package io.github.jrbase.handler.connect;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.handler.ServerCmdHandler;
import io.github.jrbase.handler.annotation.ServerCommand;

import static io.github.jrbase.dataType.ServerCmd.COMMAND;

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
