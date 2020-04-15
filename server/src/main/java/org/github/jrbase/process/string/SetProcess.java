package org.github.jrbase.process.string;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import java.util.concurrent.CompletableFuture;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.CommonMessage.*;
import static org.github.jrbase.dataType.RedisDataType.STRINGS;
import static org.github.jrbase.utils.Tools.checkArgs;
import static org.github.jrbase.utils.Tools.isEmptyBytes;

@KeyCommand
public class SetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.SET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return checkArgs(1, clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {

        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        final CompletableFuture<byte[]> future = clientCmd.getBackendProxy().getAndPut(buildUpKey, writeUtf8(clientCmd.getArgs()[0]));
        future.whenComplete((value, error) -> {
            if (isEmptyBytes(value)) {
                clientCmd.getChannel().writeAndFlush(REDIS_ONE_INTEGER);
            } else {
                clientCmd.getChannel().writeAndFlush(REDIS_ZORE_INTEGER);
            }
        });
        return REDiS_STRING_EMPTY;
    }


}
