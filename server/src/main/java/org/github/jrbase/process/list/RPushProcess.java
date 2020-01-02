package org.github.jrbase.process.list;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER;
import static org.github.jrbase.dataType.RedisDataType.LISTS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;
import static org.github.jrbase.utils.ToolsString.getRightBuildUpArgsValue;
import static org.github.jrbase.utils.ToolsString.getRightBuildUpValue;


public class RPushProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.RPUSH.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() >= 1;
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
            //only set
            StringBuilder buildUpValue = getRightBuildUpArgsValue(clientCmd.getArgs());
            //bPut
            rheaKVStore.bPut(buildUpKey, writeUtf8(buildUpValue.toString()));
            return (":" + clientCmd.getArgLength() + "\r\n");
        } else {     // update list values
            final String resultStr = readUtf8(bGetResult);
            final String[] getValueArr = resultStr.split(REDIS_LIST_DELIMITER);
            String buildUpValue = getRightBuildUpValue(clientCmd.getArgs(), getValueArr);
            //bPut
            rheaKVStore.bPut(buildUpKey, writeUtf8(buildUpValue));
            int allListLength = getValueArr.length + clientCmd.getArgLength();
            return (":" + allListLength + "\r\n");
        }
    }


}
