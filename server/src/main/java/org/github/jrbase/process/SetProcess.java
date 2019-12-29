package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.execption.ArgumentsException;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.RedisDataType.STRINGS;
import static org.github.jrbase.utils.Tools.checkArgs;


public class SetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.SET.getCmdName();
    }

    @Override
    public String process(ClientCmd clientCmd) throws ArgumentsException {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) throws ArgumentsException {
        checkArgs(1, clientCmd.getArgLength());

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        final byte[] bytes = rheaKVStore.bGetAndPut(buildUpKey, writeUtf8(clientCmd.getArgs()[0]));
        if (bytes == null) {
            return (":1\r\n");
        } else {
            return (":0\r\n");
        }

    }


}
