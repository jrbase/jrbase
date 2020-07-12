package io.github.jrbase.process.geo;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.client.utils.ToolsString.unregisterCommandStr;
import static io.github.jrbase.common.datatype.Cmd.GEORADIUS;

/**
 * TODO:
 * GEORADIUS key longitude latitude radius m|km|ft|mi
 * [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count]
 * [ASC|DESC] [STORE key] [STOREDIST key]
 */
@KeyCommand
public class GeoRadiusProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return GEORADIUS.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() >= 4 && clientCmd.getArgLength() <= 11;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return unregisterCommandStr(clientCmd);
    }

}
