package io.github.jrbase.process.sets;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

@KeyCommand
public class SCardProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return Cmd.SCARD.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
       /* final BackendProxy backendProxy = clientCmd.getBackendProxy();

        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation();
        final byte[] bGetResult = backendProxy.bGet(buildUpKey);
        if (isEmptyBytes(bGetResult)) {
            return REDIS_ZORE_INTEGER;
        } else {
            final String bGetSetsResult = readUtf8(bGetResult);
            final String[] getValueArr = bGetSetsResult.split(REDIS_LIST_DELIMITER);
            return ":" + getValueArr.length + "\r\n";
        }*/
        return "";
    }
}
