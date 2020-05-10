package io.github.jrbase.process.string;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.dataType.RedisDataType;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.utils.Tools.checkArgs;

@KeyCommand
public class GetBitProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.GETBIT.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return checkArgs(1, clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {
        clientCmd.setKey(clientCmd.getKey() + RedisDataType.STRINGS.getAbbreviation());

        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        /*final String[] args = clientCmd.getArgs();
        final String position = args[0];

        final byte[] bytes = clientCmd.getBackendProxy().bGet(clientCmd.getKey());
        StringBuilder result = new StringBuilder();
        if (isEmptyBytes(bytes)) {
            result.append(REDIS_EMPTY_STRING);
        } else {
            final int bit = Tools.getBit(position, bytes);
            if (bit == -1) {
                result.append("-ERR bit offset is not an integer or out of range\r\n");
            } else {
                result.append(":").append(bit).append("\r\n");
            }
        }
        return result.toString();*/
        return "";
    }


}
