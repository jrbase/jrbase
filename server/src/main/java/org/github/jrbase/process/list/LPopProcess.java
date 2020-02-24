package org.github.jrbase.process.list;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER;
import static org.github.jrbase.dataType.RedisDataType.LISTS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;
import static org.github.jrbase.utils.ToolsString.getLPopBuildUpValue;

@KeyCommand
public class LPopProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.LPOP.getCmdName();
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
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + LISTS.getAbbreviation();
        //bGet
        final byte[] bGetResult = rheaKVStore.bGet(buildUpKey);
        if (isEmptyBytes(bGetResult)) {
            return REDIS_EMPTY_STRING;
        } else {
            final String resultStr = readUtf8(bGetResult);
            final String[] getValueArr = resultStr.split(REDIS_LIST_DELIMITER);
            String buildUpValue = getLPopBuildUpValue(getValueArr);
            //bPut
            rheaKVStore.bPut(buildUpKey, writeUtf8(buildUpValue));
            // return left first value
            final String lPopValue = getValueArr[0];
            return ("$" + lPopValue.length() + "\r\n" + lPopValue + "\r\n");
        }
    }

}
