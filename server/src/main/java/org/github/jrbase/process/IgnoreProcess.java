package org.github.jrbase.process;

import org.github.jrbase.dataType.ClientCmd;

/**
 * error command process class
 */
public class IgnoreProcess implements CmdProcess {

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
