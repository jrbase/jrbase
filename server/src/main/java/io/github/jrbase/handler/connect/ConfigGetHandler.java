package io.github.jrbase.handler.connect;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.handler.ServerCmdHandler;
import io.github.jrbase.handler.annotation.ServerCommand;

import static io.github.jrbase.dataType.ServerCmd.CONFIG;

@ServerCommand
public class ConfigGetHandler implements ServerCmdHandler {
    @Override
    public String handle(ClientCmd clientCmd) {
//        clientCmd.getChannel()
        //config get databases
        StringBuilder result = new StringBuilder();
        result.append('*').append("2").append("\r\n");
        if ("get".equals(clientCmd.getKey()) && "databases".equals(clientCmd.getArgs()[0])) {
            result.append("$").append("9").append("\r\n").append("databases").append("\r\n");
            result.append("$").append("2").append("\r\n").append("16").append("\r\n");
        }
        return result.toString();
    }

    @Override
    public String getCmdName() {
        return CONFIG.getCmdName();
    }
}
