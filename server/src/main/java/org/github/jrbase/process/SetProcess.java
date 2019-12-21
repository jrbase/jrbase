package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.execption.MyKVException;
import org.github.jrbase.manager.CmdManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SetProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) throws MyKVException {

        requestKVAndReplyClient(clientCmd);
    }


    public void requestKVAndReplyClient(ClientCmd clientCmd) throws MyKVException {
        final Channel channel = clientCmd.getContext().channel();
        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();
        final CompletableFuture<Boolean> put = rheaKVStore.put(clientCmd.getKey(), clientCmd.getArgs()[0].getBytes());

        try {
            final Boolean aBoolean = put.get();
            if (aBoolean) {
                channel.writeAndFlush(":1\r\n");
            } else {
                channel.writeAndFlush("-set error\r\n");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new MyKVException();
        }
    }

}
