package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.execption.ArgumentsException;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.utils.Tools.checkArgs;


public class SetProcess implements CmdProcess {

    @Override
    public String process(ClientCmd clientCmd) throws ArgumentsException {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) throws ArgumentsException {
        checkArgs(1, clientCmd.getArgLength());

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        final byte[] bytes = rheaKVStore.bGetAndPut(clientCmd.getKey(), writeUtf8(clientCmd.getArgs()[0]));
        if (bytes == null) {
            return (":1\r\n");
        } else {
            return (":0\r\n");
        }

    }


}
