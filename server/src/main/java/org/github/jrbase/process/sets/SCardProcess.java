package org.github.jrbase.process.sets;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER;
import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER;
import static org.github.jrbase.dataType.RedisDataType.SETS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;

public class SCardProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return Cmd.SCARD.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation();
        final byte[] bGetResult = rheaKVStore.bGet(buildUpKey);
        if (isEmptyBytes(bGetResult)) {
            return REDIS_ZORE_INTEGER;
        } else {
            final String bGetSetsResult = readUtf8(bGetResult);
            final String[] getValueArr = bGetSetsResult.split(REDIS_LIST_DELIMITER);
            return ":" + getValueArr.length + "\r\n";
        }
    }
}
