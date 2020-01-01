package org.github.jrbase.process.list;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.RedisDataType.LISTS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;


public class LPushProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.LPUSH.getCmdName();
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
            String buildUpValue = getBuildUpArgsValue(clientCmd);
            //bPut
            rheaKVStore.bPut(buildUpKey, writeUtf8(buildUpValue));
            return (":" + clientCmd.getArgLength() + "\r\n");
        } else {     // update list values
            final String resultStr = readUtf8(bGetResult);
            final String[] valueArr = resultStr.split(",");
            String buildUpValue = getBuildUpValue(clientCmd, valueArr);
            //bPut
            rheaKVStore.bPut(buildUpKey, writeUtf8(buildUpValue));
            int allListLength = valueArr.length + clientCmd.getArgLength();
            return (":" + allListLength + "\r\n");
        }
    }

    private String getBuildUpArgsValue(ClientCmd clientCmd) {
        StringBuilder buildUpValue = new StringBuilder();
        for (String arg : clientCmd.getArgs()) {
            buildUpValue.append(arg).append(",");
        }
        if (buildUpValue.length() != 0) {
            buildUpValue.deleteCharAt(buildUpValue.length() - 1);
        }
        return buildUpValue.toString();
    }

    public static String getBuildUpValue(ClientCmd clientCmd, String[] valueArr) {
        StringBuilder buildUpValue = new StringBuilder();
        for (String s : valueArr) {
            buildUpValue.append(s).append(",");
        }
        for (String arg : clientCmd.getArgs()) {
            buildUpValue.append(arg).append(",");
        }
        if (buildUpValue.length() != 0) {
            buildUpValue.deleteCharAt(buildUpValue.length() - 1);
        }
        return buildUpValue.toString();
    }

}
