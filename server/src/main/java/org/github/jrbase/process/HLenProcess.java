package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.manager.CmdManager;
import org.github.jrbase.utils.Tools;


public class HLenProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.HASHES.getAbbreviation());

        requestKVAndReplyClient(clientCmd);
    }

    public void requestKVAndReplyClient(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getContext().channel();

        final RheaKVStore rheaKVStore = CmdManager.getClient().getRheaKVStore();

        checkKeyType();
        // llen key
        // key is first arg
        //get hash length
        String mapCountKey = clientCmd.getKey() + "h";
        final byte[] mapCountBytes = rheaKVStore.bGet(mapCountKey);
        int length = Tools.byteArrayToInt(mapCountBytes);

        channel.writeAndFlush(":" + length + "\r\n");
    }

    private void checkKeyType() {
        //TODO:
        // WRONGTYPE Operation against a key holding the wrong kind of value
    }

}
