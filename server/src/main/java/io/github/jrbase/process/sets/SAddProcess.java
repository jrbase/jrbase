package io.github.jrbase.process.sets;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;


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
        /*final BackendProxy backendProxy = clientCmd.getBackendProxy();

        String buildUpKey = clientCmd.getKey() + RedisDataType.SETS.getAbbreviation();
        final byte[] bytes = backendProxy.bGet(buildUpKey);

        final String[] args = clientCmd.getArgs();
        Set<String> argsMembers = new HashSet<>(Arrays.asList(args));

        if (Tools.isEmptyBytes(bytes)) {
            updateData(backendProxy, buildUpKey, argsMembers);
            return (":" + argsMembers.size() + "\r\n");
        } else {
            final String bGetSetsResult = readUtf8(bytes);
            final String[] getValueArr = bGetSetsResult.split(CommonMessage.REDIS_LIST_DELIMITER);
            Set<String> bGetMembers = new HashSet<>(Arrays.asList(getValueArr));
            argsMembers.addAll(bGetMembers);
            updateData(backendProxy, buildUpKey, argsMembers);
            int finalCount = argsMembers.size() - bGetMembers.size();
            return (":" + finalCount + "\r\n");
        }*/
        return "";


    }

   /* private void updateData(BackendProxy backendProxy, String buildUpKey, Set<String> argsMembers) {
        StringBuilder saddResult = new StringBuilder();
        for (String member : argsMembers) {
            saddResult.append(member).append(CommonMessage.REDIS_LIST_DELIMITER);
        }
        ToolsString.deleteLastChar(saddResult);
        backendProxy.bPut(buildUpKey, writeUtf8(saddResult.toString()));
    }*/


}
