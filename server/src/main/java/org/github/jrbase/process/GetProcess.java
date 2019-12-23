package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.manager.CmdManager;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;

public class GetProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        requestKVAndReplyClient(clientCmd);
    }

    public void requestKVAndReplyClient(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getContext().channel();
        // no args
        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();
        final byte[] bytes = rheaKVStore.bGet(clientCmd.getKey());
        if (bytes == null) {
            channel.writeAndFlush("$-1\r\n");
        } else {
            final int length = bytes.length;
            channel.writeAndFlush("$" + length + "\r\n" + readUtf8(bytes) + "\r\n");
        }
    }
}
