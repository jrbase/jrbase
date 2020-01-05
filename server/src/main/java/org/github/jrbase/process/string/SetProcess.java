package org.github.jrbase.process.string;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_ONE_INTEGER;
import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER;
import static org.github.jrbase.dataType.RedisDataType.STRINGS;
import static org.github.jrbase.utils.Tools.checkArgs;
import static org.github.jrbase.utils.Tools.isEmptyBytes;


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
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation();
        final byte[] bytes = rheaKVStore.bGetAndPut(buildUpKey, writeUtf8(clientCmd.getArgs()[0]));
        if (isEmptyBytes(bytes)) {
            return REDIS_ONE_INTEGER;
        } else {
            return REDIS_ZORE_INTEGER;
        }

    }


}
