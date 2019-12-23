package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.manager.CmdManager;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static org.github.jrbase.utils.Tools.isRightArgs;


public class HGetProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.HASHES.getAbbreviation());

        requestKVAndReplyClient(clientCmd);
    }

    public void requestKVAndReplyClient(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getContext().channel();

        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();

        if (!isRightArgs(1, clientCmd.getArgs().length)) {
            channel.writeAndFlush("-ERR wrong number of arguments for 'hget' command\r\n");
            return;
        }
        final String buildUpKey = clientCmd.getKey() + "f" + clientCmd.getArgs()[0];
        final byte[] bytes = rheaKVStore.bGet(buildUpKey);
        if (bytes == null) {
            channel.writeAndFlush("$-1\r\n");
        } else {
            final int length = bytes.length;
            channel.writeAndFlush("$" + length + "\r\n" + readUtf8(bytes) + "\r\n");
        }
    }

}
