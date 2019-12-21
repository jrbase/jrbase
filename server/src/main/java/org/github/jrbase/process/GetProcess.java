package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.execption.MyKVException;
import org.github.jrbase.manager.CmdManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GetProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) throws MyKVException {
        requestKVAndReplyClient(clientCmd);
    }

    public void requestKVAndReplyClient(ClientCmd clientCmd) throws MyKVException {
        final Channel channel = clientCmd.getContext().channel();
        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();
        final CompletableFuture<byte[]> completableFuture = rheaKVStore.get(clientCmd.getKey());
        try {
            final byte[] bytes = completableFuture.get();
            if (bytes == null) {
                channel.writeAndFlush("-key not found\r\n");
            } else {
                final int length = bytes.length;
                channel.writeAndFlush("$" + length + "\r\n" + new String(bytes) + "\r\n");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new MyKVException();
        }
    }

}
