package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.execption.ArgumentsException;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static org.github.jrbase.utils.Tools.checkArgs;


public class HGetProcess implements CmdProcess {

    @Override
    public String process(ClientCmd clientCmd) throws ArgumentsException {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.HASHES.getAbbreviation());

        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) throws ArgumentsException {
        checkArgs(1, clientCmd.getArgLength());

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        final String buildUpKey = clientCmd.getKey() + "f" + clientCmd.getArgs()[0];
        final byte[] bytes = rheaKVStore.bGet(buildUpKey);
        StringBuilder result = new StringBuilder();
        if (bytes == null) {
            result.append("$-1\r\n");
        } else {
            final int length = bytes.length;
            result.append("$").append(length).append("\r\n").append(readUtf8(bytes)).append("\r\n");
        }
        return result.toString();
    }

}
