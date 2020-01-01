package org.github.jrbase.process.list;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;

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
    public void checkArguments(ClientCmd clientCmd) {
        //ignore
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + LISTS.getAbbreviation();
        //bGet
        final byte[] bytes = rheaKVStore.bGet(buildUpKey);
        if (isEmptyBytes(bytes)) {
            return REDIS_EMPTY_STRING;
        } else {
            final String resultStr = readUtf8(bytes);
            final String[] valueArr = resultStr.split(",");

            if (valueArr.length == 0) {
                return REDIS_EMPTY_STRING;
            } else {
                // update list values
                StringBuilder buildUpValue = new StringBuilder();
                for (int i = 1; i < valueArr.length; i++) {
                    buildUpValue.append(valueArr[i]).append(",");
                }
                if (buildUpValue.length() != 0) {
                    buildUpValue.deleteCharAt(buildUpValue.length() - 1);
                }
                //bGetAndPut
                rheaKVStore.bGetAndPut(buildUpKey, writeUtf8(buildUpValue.toString()));
                // return first value
                return ("$" + valueArr[0].length() + "\r\n" + valueArr[0] + "\r\n");
            }
        }
    }

}
