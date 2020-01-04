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


public class SAddProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.SADD.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() > 1;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation();
        final String[] args = clientCmd.getArgs();
        Set<String> argsMembers = new HashSet<>(Arrays.asList(args));

        final byte[] bytes = rheaKVStore.bGet(buildUpKey);
        if (isEmptyBytes(bytes)) {
            return (":" + argsMembers.size() + "\r\n");
        } else {
            final String bGetSetsResult = readUtf8(bytes);
            final String[] getValueArr = bGetSetsResult.split(REDIS_LIST_DELIMITER);
            StringBuilder saddResult = new StringBuilder();
            Set<String> bGetMembers = new HashSet<>(Arrays.asList(getValueArr));
            argsMembers.addAll(bGetMembers);
            for (String member : argsMembers) {
                saddResult.append(member).append(REDIS_LIST_DELIMITER);
            }
            if (saddResult.length() != 0) {
                saddResult.deleteCharAt(saddResult.length() - 1);
            }
            rheaKVStore.bPut(buildUpKey, writeUtf8(saddResult.toString()));

            int finalCount = argsMembers.size() - bGetMembers.size();
            return (":" + finalCount + "\r\n");
        }

    }


}
