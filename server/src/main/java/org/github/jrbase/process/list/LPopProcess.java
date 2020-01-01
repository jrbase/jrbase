package org.github.jrbase.process.list;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.jetbrains.annotations.NotNull;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static org.github.jrbase.dataType.RedisDataType.LISTS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;


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
            final String[] valueArr = resultStr.split(",");
            String buildUpValue = getBuildUpValue(valueArr);
            //bPut
            rheaKVStore.bPut(buildUpKey, writeUtf8(buildUpValue));
            // return left first value
            return ("$" + valueArr[0].length() + "\r\n" + valueArr[0] + "\r\n");
        }
    }

    @NotNull
    public static String getBuildUpValue(String[] valueArr) {
        StringBuilder buildUpValue = new StringBuilder();
        for (int i = 1; i < valueArr.length; i++) {
            buildUpValue.append(valueArr[i]).append(",");
        }
        if (buildUpValue.length() != 0) {
            buildUpValue.deleteCharAt(buildUpValue.length() - 1);
        }
        return buildUpValue.toString();
    }

}
