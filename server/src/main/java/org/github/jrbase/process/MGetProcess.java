package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.util.ByteArray;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.manager.CmdManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MGetProcess implements CmdProcess {


    @Override
    public void process(ClientCmd clientCmd) {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        requestKVAndReplyClient(clientCmd);
    }

    public void requestKVAndReplyClient(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getContext().channel();

        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();

        List<byte[]> getList = new ArrayList<>();
        getList.add(clientCmd.getKey().getBytes());

        for (String arg : clientCmd.getArgs()) {
            getList.add(arg.getBytes());
        }
        final Map<ByteArray, byte[]> byteArrayMap = rheaKVStore.bMultiGet(getList);
        if (byteArrayMap == null) {
            channel.writeAndFlush("-any key not found\r\n");
        } else {
            StringBuffer result = new StringBuffer();
            result.append('*').append(getList.size()).append("\r\n");
            List<ByteArray> tempList = new ArrayList<>();
            for (byte[] bytes : getList) {
                tempList.add(ByteArray.wrap(bytes));
            }
            for (ByteArray key : tempList) {
                final byte[] value = byteArrayMap.get(key);
                if (value == null) {
                    result.append('$').append(-1).append("\r\n");
                } else {
                    result.append('$').append(value.length).append("\r\n").append(new String(value)).append("\r\n");
                }
            }
            channel.writeAndFlush(result);
        }

    }

}