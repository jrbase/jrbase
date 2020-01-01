package org.github.jrbase.process.string;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.RedisDataType.STRINGS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;

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

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        final String[] args = clientCmd.getArgs();
        rheaKVStore.put(buildUpKey, writeUtf8(args[0]));
        // 1 key value, key value
        // 0  1    2    3    4
        int successCount = 1;

        for (int i = 1; i < args.length; i = i + 2) {
            final byte[] buildUpArgKey = (args[i] + STRINGS.getAbbreviation()).getBytes();
            final byte[] value = writeUtf8(args[i + 1]);
            final byte[] bytes = rheaKVStore.bGetAndPut(buildUpArgKey, value);
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
