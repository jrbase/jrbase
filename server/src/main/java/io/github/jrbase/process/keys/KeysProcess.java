package io.github.jrbase.process.keys;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

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
        result.append('*').append(clientCmd.getDb().keySet().size()).append("\r\n");

        for (String key : clientCmd.getDb().keySet()) {
            result.append("$").append(key.length()).append("\r\n").append(key).append("\r\n");
        }
        return result.toString();
    }
}
