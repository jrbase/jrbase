package org.github.jrbase.handler.connect;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.handler.ServerCmdHandler;
import org.github.jrbase.handler.annotation.ServerCommand;

import static org.github.jrbase.dataType.ServerCmd.SELECT;

@ServerCommand
public class SelectHandler implements ServerCmdHandler {
    @Override
    public String handle(ClientCmd clientCmd) {
//        clientCmd.getChannel()
        clientCmd.getRedisClientContext().setDbIndex(Integer.parseInt(clientCmd.getKey()));
        return "+OK\r\n";
    }

    @Override
    public String getCmdName() {
        return SELECT.getCmdName();
    }
}
