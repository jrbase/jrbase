package org.github.jrbase.process.string;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.utils.Tools;

import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static org.github.jrbase.utils.Tools.checkArgs;
import static org.github.jrbase.utils.Tools.isEmptyBytes;

public class GetBitProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.GETBIT.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return checkArgs(1, clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        final String[] args = clientCmd.getArgs();
        final String position = args[0];

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();
        final byte[] bytes = rheaKVStore.bGet(clientCmd.getKey());
        StringBuilder result = new StringBuilder();
        if (isEmptyBytes(bytes)) {
            result.append(REDIS_EMPTY_STRING);
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
