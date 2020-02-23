package org.github.jrbase.process.string;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.util.ByteArray;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static org.github.jrbase.dataType.RedisDataType.STRINGS;

@KeyCommand
public class MGetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.MGET.getCmdName();
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
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();
        // key is first arg
        List<byte[]> keyList = new ArrayList<>();
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        keyList.add(buildUpKey.getBytes());

        for (String arg : clientCmd.getArgs()) {
            keyList.add((arg + STRINGS.getAbbreviation()).getBytes());
        }
        // key, value
        // new ByteArray("key".getBytes()), "value".getBytes()
        final Map<ByteArray, byte[]> multiGetResult = rheaKVStore.bMultiGet(keyList);

        StringBuilder result = new StringBuilder();
        result.append('*').append(keyList.size()).append("\r\n");
        List<ByteArray> tempList = new ArrayList<>();
        for (byte[] bytes : keyList) {
            tempList.add(ByteArray.wrap(bytes));
        }
        for (ByteArray key : tempList) {
            final byte[] value = multiGetResult.get(key);
            if (value == null) {
                result.append('$').append(-1).append("\r\n");
            } else {
                result.append('$').append(value.length).append("\r\n").append(readUtf8(value)).append("\r\n");
            }
        }
        return result.toString();

    }

}
