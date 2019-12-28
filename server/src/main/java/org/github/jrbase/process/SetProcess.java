package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.execption.ArgumentsException;
import org.github.jrbase.manager.CmdManager;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.utils.Tools.checkArgs;


public class SetProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) throws ArgumentsException {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        requestKVAndReplyClient(clientCmd);
    }


    public void requestKVAndReplyClient(ClientCmd clientCmd) throws ArgumentsException {
        checkArgs(1, clientCmd.getArgLength());

        final Channel channel = clientCmd.getContext().channel();
        final RheaKVStore rheaKVStore = CmdManager.getRheaKVStore();


        final byte[] bytes = rheaKVStore.bGetAndPut(clientCmd.getKey(), writeUtf8(clientCmd.getArgs()[0]));
        if (bytes == null) {
            channel.writeAndFlush(":1\r\n");
        } else {
            channel.writeAndFlush(":0\r\n");
        }

    }


}
