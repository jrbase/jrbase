package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.execption.ArgumentsException;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;

public class MSetProcess implements CmdProcess {

    @Override
    public String process(ClientCmd clientCmd) throws ArgumentsException {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) throws ArgumentsException {

        //
        if (isWrongArgs(clientCmd)) {
            throw new ArgumentsException();
        }

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        final String[] args = clientCmd.getArgs();
        rheaKVStore.put(clientCmd.getKey(), writeUtf8(args[0]));
        // 1 key value, key value
        // 0  1    2    3    4
        int successCount = 1;

        for (int i = 1; i < args.length; i = i + 2) {
            final byte[] key = args[i].getBytes();
            final byte[] value = writeUtf8(args[i + 1]);
            final byte[] bytes = rheaKVStore.bGetAndPut(key, value);
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
