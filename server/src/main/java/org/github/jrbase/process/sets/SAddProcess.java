package org.github.jrbase.process.sets;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER;
import static org.github.jrbase.dataType.RedisDataType.SETS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;
import static org.github.jrbase.utils.ToolsString.deleteLastChar;


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
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation();
        final byte[] bytes = rheaKVStore.bGet(buildUpKey);

        final String[] args = clientCmd.getArgs();
        Set<String> argsMembers = new HashSet<>(Arrays.asList(args));

        if (isEmptyBytes(bytes)) {
            updateData(rheaKVStore, buildUpKey, argsMembers);
            return (":" + argsMembers.size() + "\r\n");
        } else {
            final String bGetSetsResult = readUtf8(bytes);
            final String[] getValueArr = bGetSetsResult.split(REDIS_LIST_DELIMITER);
            Set<String> bGetMembers = new HashSet<>(Arrays.asList(getValueArr));
            argsMembers.addAll(bGetMembers);
            updateData(rheaKVStore, buildUpKey, argsMembers);
            int finalCount = argsMembers.size() - bGetMembers.size();
            return (":" + finalCount + "\r\n");
        }

    }

    private void updateData(RheaKVStore rheaKVStore, String buildUpKey, Set<String> argsMembers) {
        StringBuilder saddResult = new StringBuilder();
        for (String member : argsMembers) {
            saddResult.append(member).append(REDIS_LIST_DELIMITER);
        }
        deleteLastChar(saddResult);
        rheaKVStore.bPut(buildUpKey, writeUtf8(saddResult.toString()));
    }


}
