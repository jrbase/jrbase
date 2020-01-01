package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.dataType.RedisDataType;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static org.github.jrbase.dataType.RedisDataType.KEYS;

public class TypeProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.TYPE.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        // no args
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();
        String buildUpKey = clientCmd.getKey() + KEYS.getAbbreviation();
        final byte[] getValue = rheaKVStore.bGet(buildUpKey);
        StringBuilder result = new StringBuilder();
        if (getValue == null) {
            result.append("$").append(RedisDataType.NONE.getName().length()).append("\r\n").append(RedisDataType.NONE.getName()).append("\r\n");
        } else {
            final RedisDataType redisDataType = RedisDataType.get(readUtf8(getValue));
            final int length = redisDataType.getName().length();
            result.append("$").append(length).append("\r\n").append(redisDataType.getName()).append("\r\n");
        }
        return result.toString();

    }
}
