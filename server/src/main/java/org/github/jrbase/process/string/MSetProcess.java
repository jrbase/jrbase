package org.github.jrbase.process.string;

import org.github.jrbase.backend.BackendProxy;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.RedisDataType.STRINGS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;

@KeyCommand
public class MSetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.MSET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return !isWrongArgs(clientCmd);
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        final BackendProxy backendProxy = clientCmd.getBackendProxy();

        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        final String[] args = clientCmd.getArgs();
        backendProxy.bPut(buildUpKey, writeUtf8(args[0]));
        // 1 key value, key value
        // 0  1    2    3    4
        int successCount = 1;

        for (int i = 1; i < args.length; i = i + 2) {
            final String buildUpArgKey = args[i] + STRINGS.getAbbreviation();
            final byte[] value = writeUtf8(args[i + 1]);
            final byte[] bytes = backendProxy.bGetAndPut(buildUpArgKey, value);
            if (isEmptyBytes(bytes)) {
                successCount++;
            }
        }
        return (":" + successCount + "\r\n");

    }

    private boolean isWrongArgs(ClientCmd clientCmd) {
        return clientCmd.getArgLength() <= 0 || clientCmd.getArgLength() % 2 == 0;
    }

}
