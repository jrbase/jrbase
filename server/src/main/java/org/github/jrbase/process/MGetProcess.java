package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.util.ByteArray;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.execption.MyKVException;
import org.github.jrbase.manager.CmdManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MGetProcess implements CmdProcess {


    @Override
    public void process(ClientCmd clientCmd) throws MyKVException {
        requestKVAndReplyClient(clientCmd);
    }

    public void requestKVAndReplyClient(ClientCmd clientCmd) throws MyKVException {
        final Channel channel = clientCmd.getContext().channel();

        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();

        List<byte[]> getList = new ArrayList<>();
        getList.add(clientCmd.getKey().getBytes());

        for (String arg : clientCmd.getArgs()) {
            getList.add(arg.getBytes());
        }
        final CompletableFuture<Map<ByteArray, byte[]>> completableFuture = rheaKVStore.multiGet(getList);
        try {
            final Map<ByteArray, byte[]> byteArrayMap = completableFuture.get();
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
        } catch (InterruptedException | ExecutionException e) {
            throw new MyKVException();
        }
    }

}
