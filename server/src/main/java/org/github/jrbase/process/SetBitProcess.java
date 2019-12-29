package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.execption.ArgumentsException;
import org.github.jrbase.utils.Tools;

import static org.github.jrbase.utils.Tools.checkArgs;


public class SetBitProcess implements CmdProcess {

    @Override
    public String getName() {
        return Cmd.SETBIT.getCmdName();
    }

    @Override
    public String process(ClientCmd clientCmd) throws ArgumentsException {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) throws ArgumentsException {
        checkArgs(2, clientCmd.getArgLength());
        //setbit key 2 1
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        final byte[] bytes = rheaKVStore.bGet(clientCmd.getKey());
        if (bytes == null) {
            return (":0\r\n");
        } else {
            final String[] args = clientCmd.getArgs();
            final int lastBit = Tools.getBit(args[0], bytes);

            final int result = Tools.setBit(args[0], args[1], bytes);
            // update bytes
            rheaKVStore.put(clientCmd.getKey(), bytes);

            if (result == -1) {
                return ("-ERR bit offset is not an integer or out of range\r\n");
            } else {
                return (":" + lastBit + "\r\n");
            }
        }

    }


}
