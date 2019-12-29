package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.execption.ArgumentsException;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.RedisDataType.STRINGS;

public class MSetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.MSET.getCmdName();
    }

    @Override
    public String process(ClientCmd clientCmd) throws ArgumentsException {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) throws ArgumentsException {

        //
        if (isWrongArgs(clientCmd)) {
            throw new ArgumentsException();
        }

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
            if (bytes == null) {
                successCount++;
            }
        }
        return (":" + successCount + "\r\n");

    }

    private boolean isWrongArgs(ClientCmd clientCmd) {
        return clientCmd.getArgLength() <= 0 || clientCmd.getArgLength() % 2 == 0;
    }

}
