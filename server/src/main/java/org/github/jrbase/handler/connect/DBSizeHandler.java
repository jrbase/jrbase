package org.github.jrbase.handler.connect;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.handler.ServerCmdHandler;
import org.github.jrbase.handler.annotation.ServerCommand;

import static org.github.jrbase.dataType.ServerCmd.DBSIZE;

@ServerCommand
public class DBSizeHandler implements ServerCmdHandler {
    @Override
    public String handle(ClientCmd clientCmd) {
        return ":" + clientCmd.getDb().getTable().size() + "\r\n";
    }

    @Override
    public String getCmdName() {
        return DBSIZE.getCmdName();
    }
}
