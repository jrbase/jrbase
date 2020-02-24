package org.github.jrbase.process.hash;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;
import org.github.jrbase.utils.Tools;

import static org.github.jrbase.dataType.RedisDataType.HASHES;

@KeyCommand
public class HLenProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.HLEN.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        checkKeyType();

        // llen key
        // key is first arg
        //get hash length
        String mapCountKey = clientCmd.getKey() + HASHES.getAbbreviation();
        final byte[] mapCountBytes = clientCmd.getBackendProxy().bGet(mapCountKey);
        int length = Tools.byteArrayToInt(mapCountBytes);
        return ":" + length + "\r\n";
    }

    private void checkKeyType() {
        //TODO:
        // WRONGTYPE Operation against a key holding the wrong kind of value
    }

}
