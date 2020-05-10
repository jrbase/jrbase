package io.github.jrbase.process.string;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;
import io.github.jrbase.utils.Tools;

@KeyCommand
public class SetBitProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.SETBIT.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return Tools.checkArgs(2, clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        //setbit key 2 1
        /*final BackendProxy backendProxy = clientCmd.getBackendProxy();

        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        final byte[] bytes = backendProxy.bGet(buildUpKey);
        if (Tools.isEmptyBytes(bytes)) {
            return REDIS_ZORE_INTEGER;
        } else {
            final String[] args = clientCmd.getArgs();
            final int lastBit = Tools.getBit(args[0], bytes);

            final int result = Tools.setBit(args[0], args[1], bytes);
            // update bytes
            backendProxy.bPut(buildUpKey, bytes);

            if (result == -1) {
                return ("-ERR bit offset is not an integer or out of range\r\n");
            } else {
                return (":" + lastBit + "\r\n");
            }
        }*/
        return "";

    }


}
