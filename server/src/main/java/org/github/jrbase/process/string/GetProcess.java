package org.github.jrbase.process.string;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import java.util.concurrent.CompletableFuture;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static org.github.jrbase.dataType.CommonMessage.REDiS_STRING_EMPTY;
import static org.github.jrbase.dataType.RedisDataType.STRINGS;

@KeyCommand
public class GetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.GET.getCmdName();
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
        // no args
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        final CompletableFuture<byte[]> future = clientCmd.getBackendProxy().get(buildUpKey);
        future.whenComplete((getValue, error) -> {
            // TODO: handle error

            StringBuilder result = new StringBuilder();
            if (getValue == null) {
                result.append(REDIS_EMPTY_STRING);
            } else {
                final int length = getValue.length;
                result.append("$").append(length).append("\r\n").append(readUtf8(getValue)).append("\r\n");
            }
            clientCmd.getChannel().writeAndFlush(result.toString());
        });
        return REDiS_STRING_EMPTY;
    }
}
