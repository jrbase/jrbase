package io.github.jrbase.process.geo;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.common.datatype.Cmd.GEOHASH;

@KeyCommand
public class GeoHashProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return GEOHASH.getCmdName();
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
