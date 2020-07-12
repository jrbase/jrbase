package io.github.jrbase.process;

import io.github.jrbase.common.datatype.Cmd;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.client.utils.ToolsString.unregisterCommandStr;

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
        return unregisterCommandStr(clientCmd);
    }

}
