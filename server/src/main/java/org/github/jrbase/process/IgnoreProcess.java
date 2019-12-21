package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.ChannelHandlerContext;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.execption.MyKVException;
import org.github.jrbase.manager.CmdManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class IgnoreProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) {
        clientCmd.getContext().channel().writeAndFlush("+Command not implement: " + clientCmd.getCmd() + "\r\n");
    }

    @Override
    public void requestKVAndReplyClient() {

    }

}
