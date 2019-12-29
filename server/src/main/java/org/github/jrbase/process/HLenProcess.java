package org.github.jrbase.process;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.utils.Tools;


public class HLenProcess implements CmdProcess {

    @Override
    public String getName() {
        return Cmd.HLEN.getCmdName();
    }

    @Override
    public String process(ClientCmd clientCmd) {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.HASHES.getAbbreviation());

        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {

        checkKeyType();

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();
        // llen key
        // key is first arg
        //get hash length
        String mapCountKey = clientCmd.getKey() + "h";
        final byte[] mapCountBytes = rheaKVStore.bGet(mapCountKey);
        int length = Tools.byteArrayToInt(mapCountBytes);
        return ":" + length + "\r\n";
    }

    private void checkKeyType() {
        //TODO:
        // WRONGTYPE Operation against a key holding the wrong kind of value
    }

}
