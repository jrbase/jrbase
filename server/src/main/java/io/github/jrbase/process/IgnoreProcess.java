package io.github.jrbase.process;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.process.annotation.KeyCommand;

/**
 * error command process class
 */

@KeyCommand
public class IgnoreProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.OTHER.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        StringBuilder result = new StringBuilder();
        result.append("-ERR unknown command '").append(clientCmd.getCmd()).append("', with args beginning with:");
        if (!clientCmd.getKey().isEmpty()) {
            result.append(clientCmd.getKey()).append(", ");
        }
        for (String arg : clientCmd.getArgs()) {
            result.append(arg).append(", ");
        }
        result.append("\r\n");
        return result.toString();
    }

}
