package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static org.github.jrbase.dataType.RedisDataType.STRINGS;

public class GetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.GET.getCmdName();
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        // no args
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        final byte[] getValue = rheaKVStore.bGet(buildUpKey);
        StringBuilder result = new StringBuilder();
        if (getValue == null) {
            result.append("$-1\r\n");
        } else {
            final int length = getValue.length;
            result.append("$").append(length).append("\r\n").append(readUtf8(getValue)).append("\r\n");
        }
        return result.toString();

    }
}
