package org.github.jrbase.process.list;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.execption.ArgumentsException;
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
    public void checkArguments(ClientCmd clientCmd) throws ArgumentsException {
        if (clientCmd.getArgLength() < 1) {
            throw new ArgumentsException();
        }
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + LISTS.getAbbreviation();
        final byte[] bytes = rheaKVStore.bGet(buildUpKey);
        if (isEmptyBytes(bytes)) {
            //only set
            StringBuilder buildUpValue = new StringBuilder();
            for (String arg : clientCmd.getArgs()) {
                buildUpValue.append(arg).append(",");
            }
            if (buildUpValue.length() != 0) {
                buildUpValue.deleteCharAt(buildUpValue.length() - 1);
            }
            rheaKVStore.bPut(buildUpKey, writeUtf8(buildUpValue.toString()));
            return (":" + clientCmd.getArgLength() + "\r\n");
        } else {
            final String resultStr = readUtf8(bytes);
            final String[] valueArr = resultStr.split(",");
            if (valueArr.length == 0) {
                return (":" + clientCmd.getArgLength() + "\r\n");
            } else {
                // update list values
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

                rheaKVStore.bPut(buildUpKey, writeUtf8(buildUpValue.toString()));

                int allListLength = valueArr.length + clientCmd.getArgLength();
                return (":" + allListLength + "\r\n");
            }
        }
    }

}
