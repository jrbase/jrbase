package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.manager.CmdManager;
import org.github.jrbase.utils.Tools;

public class HSetProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.HASHES.getAbbreviation());

        requestKVAndReplyClient(clientCmd);
    }

    public void requestKVAndReplyClient(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getContext().channel();
        // hset key field value
        final int argsLength = clientCmd.getArgs().length;
        if (!isRightArgs(argsLength)) {
            channel.writeAndFlush("-ERR wrong number of arguments for 'hset' command\r\n");
            return;
        }
        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();

        final String key = clientCmd.getKey();
        final String[] args = clientCmd.getArgs();

        //1 get mapCount
        String mapCountKey = key + "h";
        final byte[] mapCountBytes = rheaKVStore.bGet(mapCountKey);
        int mapCount = Tools.byteArrayToInt(mapCountBytes);

        //2 put hset
        int successCount = 0;
        for (int i = 0; i < argsLength; i = i + 2) {
            final String field = args[i];
            final String value = args[i + 1];
            String buildUpKey = key + "f" + field;
            final byte[] bytes = rheaKVStore.bGetAndPut(buildUpKey, value.getBytes());
            successCount = bytes == null ? successCount + 1 : successCount;
        }

        //3 get successCount then mapCount = mapCount + successCount
        mapCount = mapCount + successCount;

        //4 put mapCount
        rheaKVStore.put(mapCountKey, Tools.intToByteArray(mapCount));

        channel.writeAndFlush(":" + successCount + "\r\n");
        // another way,but cant't get successCount //    kvList.add(new KVEntry(buildUpKey.getBytes(), value.getBytes()));

    }

    private boolean isRightArgs(int length) {
        return length >= 2 && length % 2 == 0;
    }

}
