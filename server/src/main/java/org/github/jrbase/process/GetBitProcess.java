package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.manager.CmdManager;
import org.github.jrbase.utils.Tools;

import static org.github.jrbase.utils.Tools.isRightArgs;

public class GetBitProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        requestKVAndReplyClient(clientCmd);
    }

    public void requestKVAndReplyClient(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getContext().channel();
        if (!isRightArgs(1, clientCmd.getArgs().length)) {
            channel.writeAndFlush("-ERR wrong number of arguments for 'getbit' command\r\n");
            return;
        }
        final String[] args = clientCmd.getArgs();
        final String position = args[0];

        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();
        final byte[] bytes = rheaKVStore.bGet(clientCmd.getKey());
        if (bytes == null) {
            channel.writeAndFlush("$-1\r\n");
        } else {
            final int bit = Tools.getBit(position, bytes);
            if (bit == -1) {
                channel.writeAndFlush("-ERR bit offset is not an integer or out of range\r\n");
            } else {
                channel.writeAndFlush(":" + bit + "\r\n");
            }
        }
    }


}
