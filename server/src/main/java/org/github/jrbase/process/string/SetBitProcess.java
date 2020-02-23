package org.github.jrbase.process.string;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;
import org.github.jrbase.utils.Tools;

import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER;
import static org.github.jrbase.dataType.RedisDataType.STRINGS;
import static org.github.jrbase.utils.Tools.checkArgs;
import static org.github.jrbase.utils.Tools.isEmptyBytes;

@KeyCommand
public class SetBitProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.SETBIT.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return checkArgs(2, clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        //setbit key 2 1
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        final byte[] bytes = rheaKVStore.bGet(buildUpKey);
        if (isEmptyBytes(bytes)) {
            return REDIS_ZORE_INTEGER;
        } else {
            final String[] args = clientCmd.getArgs();
            final int lastBit = Tools.getBit(args[0], bytes);

            final int result = Tools.setBit(args[0], args[1], bytes);
            // update bytes
            rheaKVStore.bPut(buildUpKey, bytes);

            if (result == -1) {
                return ("-ERR bit offset is not an integer or out of range\r\n");
            } else {
                return (":" + lastBit + "\r\n");
            }
        }

    }


}
