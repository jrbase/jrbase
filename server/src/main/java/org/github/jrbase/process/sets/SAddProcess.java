package org.github.jrbase.process.sets;

import org.github.jrbase.backend.BackendProxy;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER;
import static org.github.jrbase.dataType.RedisDataType.SETS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;
import static org.github.jrbase.utils.ToolsString.deleteLastChar;

@KeyCommand
public class SAddProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.SADD.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() > 0;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        final BackendProxy backendProxy = clientCmd.getBackendProxy();

        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation();
        final byte[] bytes = backendProxy.bGet(buildUpKey);

        final String[] args = clientCmd.getArgs();
        Set<String> argsMembers = new HashSet<>(Arrays.asList(args));

        if (isEmptyBytes(bytes)) {
            updateData(backendProxy, buildUpKey, argsMembers);
            return (":" + argsMembers.size() + "\r\n");
        } else {
            final String bGetSetsResult = readUtf8(bytes);
            final String[] getValueArr = bGetSetsResult.split(REDIS_LIST_DELIMITER);
            Set<String> bGetMembers = new HashSet<>(Arrays.asList(getValueArr));
            argsMembers.addAll(bGetMembers);
            updateData(backendProxy, buildUpKey, argsMembers);
            int finalCount = argsMembers.size() - bGetMembers.size();
            return (":" + finalCount + "\r\n");
        }

    }

    private void updateData(BackendProxy backendProxy, String buildUpKey, Set<String> argsMembers) {
        StringBuilder saddResult = new StringBuilder();
        for (String member : argsMembers) {
            saddResult.append(member).append(REDIS_LIST_DELIMITER);
        }
        deleteLastChar(saddResult);
        backendProxy.bPut(buildUpKey, writeUtf8(saddResult.toString()));
    }


}
