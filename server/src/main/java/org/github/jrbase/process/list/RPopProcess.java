package org.github.jrbase.process.list;

import org.github.jrbase.backend.BackendProxy;
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
import static org.github.jrbase.utils.ToolsString.getRPopBuildUpValue;

@KeyCommand
public class RPopProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.RPOP.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    // a b c d
    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        final BackendProxy backendProxy = clientCmd.getBackendProxy();

        String buildUpKey = clientCmd.getKey() + LISTS.getAbbreviation();
        //bGet
        final byte[] bGetResult = backendProxy.bGet(buildUpKey);
        if (isEmptyBytes(bGetResult)) {
            return REDIS_EMPTY_STRING;
        } else {
            final String resultStr = readUtf8(bGetResult);
            final String[] getValueArr = resultStr.split(REDIS_LIST_DELIMITER);
            String buildUpValue = getRPopBuildUpValue(getValueArr);
            //bPut
            backendProxy.bPut(buildUpKey, writeUtf8(buildUpValue));
            // return left first value
            final String rPopValue = getValueArr[getValueArr.length - 1];
            return ("$" + rPopValue.length() + "\r\n" + rPopValue + "\r\n");
        }
    }

}
