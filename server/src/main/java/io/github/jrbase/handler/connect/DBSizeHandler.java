package io.github.jrbase.handler.connect;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.handler.ServerCmdHandler;
import io.github.jrbase.handler.annotation.ServerCommand;

import static io.github.jrbase.dataType.ServerCmd.DBSIZE;

@ServerCommand
public class DBSizeHandler implements ServerCmdHandler {
    @Override
    public String handle(ClientCmd clientCmd) {
        return ":" + clientCmd.getDb().size() + "\r\n";
    }

    @Override
    public String getCmdName() {
        return DBSIZE.getCmdName();
    }
}
