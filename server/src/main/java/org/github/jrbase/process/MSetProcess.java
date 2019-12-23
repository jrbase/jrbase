package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.manager.CmdManager;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;

public class MSetProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        requestKVAndReplyClient(clientCmd);
    }


    public void requestKVAndReplyClient(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getContext().channel();

        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();

        final String[] args = clientCmd.getArgs();
        if (args.length <= 0) {
            channel.writeAndFlush("-ERR wrong number of arguments for 'mset' command\r\n");
            return;
        }
        rheaKVStore.put(clientCmd.getKey(), writeUtf8(args[0]));
        // 1 key value, key value
        // 0  1    2    3    4
        int successCount = 1;
        if (args.length % 2 == 1) {
            for (int i = 1; i < args.length; i = i + 2) {
                final byte[] key = args[i].getBytes();
                final byte[] value = writeUtf8(args[i + 1]);
                final byte[] bytes = rheaKVStore.bGetAndPut(key, value);
                if (bytes == null) {
                    successCount++;
                }
            }
            channel.writeAndFlush(":" + successCount + "\r\n");
        } else {
            channel.writeAndFlush("-mset argument error\r\n");
        }
    }

}
