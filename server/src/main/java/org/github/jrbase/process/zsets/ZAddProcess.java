package org.github.jrbase.process.zsets;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.apache.commons.lang.StringUtils;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER;
import static org.github.jrbase.dataType.RedisDataType.SORTED_SETS;
import static org.github.jrbase.utils.ToolsKeyValue.generateKeyValueMap;

/**
 * ZADD key [NX|XX] [CH] [INCR] score member [score member ...]
 */
public class ZAddProcess implements CmdProcess {
    // TODO: parse [NX|XX] [CH] [INCR]
    @Override
    public String getCmdName() {
        return Cmd.ZADD.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() >= 2;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + SORTED_SETS.getAbbreviation();
        final byte[] bytes = rheaKVStore.bGet(buildUpKey);
        final String kvResult = readUtf8(bytes);
        final Map<String, String> KeyValueMap = generateKeyValueMap(clientCmd.getArgs());

        if (StringUtils.isEmpty(kvResult)) {
            String result = getKvBuildUpResult(KeyValueMap);
            rheaKVStore.bPut(buildUpKey, result.getBytes());
            return ":" + KeyValueMap.size() + "\r\n";
        } else {
            //update
            final String[] arr = kvResult.split(REDIS_LIST_DELIMITER);
            final Map<String, String> bGetKvMap = generateKeyValueMap(arr);
            int resultCount = 0;
            for (String key : KeyValueMap.keySet()) {
                if (bGetKvMap.get(key) == null) {
                    resultCount += 1;
                }
            }
            bGetKvMap.putAll(KeyValueMap);
            String result = getKvBuildUpResult(bGetKvMap);
            rheaKVStore.bPut(buildUpKey, result.getBytes());
            return ":" + resultCount + "\r\n";

        }
    }

    @NotNull
    public static String getKvBuildUpResult(Map<String, String> generateKeyValueMap) {
        StringBuilder result = new StringBuilder();
        for (String key : generateKeyValueMap.keySet()) {
            result.append(key).append(REDIS_LIST_DELIMITER).append(generateKeyValueMap.get(key)).append(REDIS_LIST_DELIMITER);
        }
        return result.toString();
    }


}
