package org.github.jrbase.process.hash;

import org.github.jrbase.backend.BackendProxy;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;
import org.github.jrbase.utils.Tools;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.RedisDataType.HASHES;
import static org.github.jrbase.utils.ToolsKeyValue.generateKeyValueMap;
import static org.github.jrbase.utils.ToolsKeyValue.keyValueEventNumber;

@KeyCommand
public class HSetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.HSET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return keyValueEventNumber(clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {

        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        // hset key field value
        final BackendProxy backendProxy = clientCmd.getBackendProxy();

        //1 get mapCount
        String mapCountKey = clientCmd.getKey() + "h";
        final byte[] mapCountBytes = backendProxy.bGet(mapCountKey);
        final int originCount = Tools.byteArrayToInt(mapCountBytes);
        //2 put hset
        //3 get successCount
        final int successCount = getSuccessCountUpdate(clientCmd, backendProxy);
        int totalCount = originCount + successCount;

        //4 update totalCount
        backendProxy.bPut(mapCountKey, Tools.intToByteArray(totalCount));
        return ":" + successCount + "\r\n";

        // another think,but cant't implement
        // kvList.add(new KVEntry(buildUpKey, writeUtf8(value)));
    }

    private int getSuccessCountUpdate(ClientCmd clientCmd, BackendProxy backendProxy) {
        int successCount = 0;
        final Map<String, String> keyValueMap = generateKeyValueMap(clientCmd.getArgs());
        for (String field : keyValueMap.keySet()) {
            String buildUpKey = getBuildUpKey(clientCmd.getKey(), field);
            final byte[] bytes = backendProxy.bGetAndPut(buildUpKey, writeUtf8(keyValueMap.get(field)));
            successCount = bytes == null ? successCount + 1 : successCount;
        }
        return successCount;
    }

    @NotNull
    private String getBuildUpKey(String key, String field) {
        return key + "f" + field + HASHES.getAbbreviation();
    }

}
