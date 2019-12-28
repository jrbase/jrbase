package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.execption.ArgumentsException;
import org.github.jrbase.manager.CmdManager;
import org.github.jrbase.utils.Tools;

import static org.github.jrbase.utils.Tools.checkArgs;


public class SetBitProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) throws ArgumentsException {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        requestKVAndReplyClient(clientCmd);
    }


    public void requestKVAndReplyClient(ClientCmd clientCmd) throws ArgumentsException {
        checkArgs(2, clientCmd.getArgLength());
        //setbit key 2 1
        final Channel channel = clientCmd.getContext().channel();
        final RheaKVStore rheaKVStore = CmdManager.getRheaKVStore();
        final byte[] bytes = rheaKVStore.bGet(clientCmd.getKey());
        if (bytes == null) {
            channel.writeAndFlush(":0\r\n");
        } else {
            final String[] args = clientCmd.getArgs();
            final int lastBit = Tools.getBit(args[0], bytes);

            final int result = Tools.setBit(args[0], args[1], bytes);
            // update bytes
            rheaKVStore.put(clientCmd.getKey(), bytes);
            if (result == -1) {
                channel.writeAndFlush("-ERR bit offset is not an integer or out of range\r\n");
            } else {
                channel.writeAndFlush(":" + lastBit + "\r\n");
            }
        }

    }


}
