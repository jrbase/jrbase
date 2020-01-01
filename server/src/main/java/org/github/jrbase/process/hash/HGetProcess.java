package org.github.jrbase.process.hash;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static org.github.jrbase.dataType.RedisDataType.HASHES;
import static org.github.jrbase.utils.Tools.checkArgs;
import static org.github.jrbase.utils.Tools.isEmptyBytes;


public class HGetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.HGET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return checkArgs(1, clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {

        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        final String buildUpKey = clientCmd.getKey() + "f" + clientCmd.getArgs()[0] + HASHES.getAbbreviation();
        final byte[] bytes = rheaKVStore.bGet(buildUpKey);
        StringBuilder result = new StringBuilder();
        if (isEmptyBytes(bytes)) {
            result.append(REDIS_EMPTY_STRING);
        } else {
            final int length = bytes.length;
            result.append("$").append(length).append("\r\n").append(readUtf8(bytes)).append("\r\n");
        }
        return result.toString();
    }

}
