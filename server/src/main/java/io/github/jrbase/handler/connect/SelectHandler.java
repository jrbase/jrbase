package io.github.jrbase.handler.connect;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.handler.ServerCmdHandler;
import io.github.jrbase.handler.annotation.ServerCommand;

import static io.github.jrbase.dataType.ServerCmd.SELECT;

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
