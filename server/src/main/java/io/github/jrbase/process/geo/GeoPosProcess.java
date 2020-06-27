package io.github.jrbase.process.geo;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.common.datatype.Cmd.GEOPOS;

@KeyCommand
public class GeoPosProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return GEOPOS.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return false;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return null;
    }
}
