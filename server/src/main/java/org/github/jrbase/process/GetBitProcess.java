package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.execption.ArgumentsException;
import org.github.jrbase.utils.Tools;

import static org.github.jrbase.utils.Tools.checkArgs;

public class GetBitProcess implements CmdProcess {

    @Override
    public String process(ClientCmd clientCmd) throws ArgumentsException {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) throws ArgumentsException {
        checkArgs(1, clientCmd.getArgLength());

        final String[] args = clientCmd.getArgs();
        final String position = args[0];

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();
        final byte[] bytes = rheaKVStore.bGet(clientCmd.getKey());
        StringBuilder result = new StringBuilder();
        if (bytes == null) {
            result.append("$-1\r\n");
        } else {
            final int bit = Tools.getBit(position, bytes);
            if (bit == -1) {
                result.append("-ERR bit offset is not an integer or out of range\r\n");
            } else {
                result.append(":").append(bit).append("\r\n");
            }
        }
        return result.toString();
    }


}
