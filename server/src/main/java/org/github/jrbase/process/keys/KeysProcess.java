package org.github.jrbase.process.keys;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

@KeyCommand
public class KeysProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.KEYS.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() == 0;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        //set key currentTime + args[0]ms
        StringBuilder result = new StringBuilder();
        result.append('*').append(clientCmd.getDb().getTable().keySet().size()).append("\r\n");

        for (String key : clientCmd.getDb().getTable().keySet()) {
            result.append("$").append(key.length()).append("\r\n").append(key).append("\r\n");
        }
        return result.toString();
    }
}
