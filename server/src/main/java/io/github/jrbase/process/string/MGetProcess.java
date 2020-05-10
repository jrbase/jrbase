package io.github.jrbase.process.string;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import java.util.ArrayList;
import java.util.List;

import static io.github.jrbase.dataType.RedisDataType.STRINGS;

@KeyCommand
public class MGetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.MGET.getCmdName();
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
        // key is first arg
        List<byte[]> keyList = new ArrayList<>();
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        keyList.add(buildUpKey.getBytes());

        for (String arg : clientCmd.getArgs()) {
            keyList.add((arg + STRINGS.getAbbreviation()).getBytes());
        }

        return "";

    }

}
