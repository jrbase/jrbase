package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.execption.MyKVException;
import org.github.jrbase.manager.CmdManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HSetProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) throws MyKVException {
        requestKVAndReplyClient(clientCmd);
    }

    public void requestKVAndReplyClient(ClientCmd clientCmd) throws MyKVException {
        final Channel channel = clientCmd.getContext().channel();

        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();
        // hset key key1 value1
        final String key = clientCmd.getKey();
        if (clientCmd.getArgs().length < 2) {
            channel.writeAndFlush("-hset args error\r\n");
            return;
        }
        //TODO:  command codec specification
        final String key1 = clientCmd.getArgs()[0];
        final String value1 = clientCmd.getArgs()[1];
        String value = key1 + "||" + value1;
        // set redisClient's data to TiKV
        final CompletableFuture<Boolean> future = rheaKVStore.put(clientCmd.getKey(), value.getBytes());

        try {
            final Boolean success = future.get();
            if (success) {
                channel.writeAndFlush(":1\r\n");
            } else {
                channel.writeAndFlush("-hset error\r\n");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new MyKVException();
        }
    }

}
