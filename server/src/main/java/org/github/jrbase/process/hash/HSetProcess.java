package org.github.jrbase.process.hash;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.utils.Tools;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.RedisDataType.HASHES;

public class HSetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.HSET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return isRightArgs(clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {

        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        // hset key field value

        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        final String key = clientCmd.getKey();
        final String[] args = clientCmd.getArgs();

        //1 get mapCount
        String mapCountKey = key + "h";
        final byte[] mapCountBytes = rheaKVStore.bGet(mapCountKey);
        int mapCount = Tools.byteArrayToInt(mapCountBytes);

        //2 put hset
        int successCount = 0;
        for (int i = 0; i < clientCmd.getArgLength(); i = i + 2) {
            final String field = args[i];
            final String value = args[i + 1];
            String buildUpKey = key + "f" + field + HASHES.getAbbreviation();
            final byte[] bytes = rheaKVStore.bGetAndPut(buildUpKey, writeUtf8(value));
            successCount = bytes == null ? successCount + 1 : successCount;
        }

        //3 get successCount then mapCount = mapCount + successCount
        mapCount = mapCount + successCount;

        //4 put mapCount
        rheaKVStore.put(mapCountKey, Tools.intToByteArray(mapCount));
        return ":" + successCount + "\r\n";


        // another way,but cant't get successCount
        // kvList.add(new KVEntry(buildUpKey, writeUtf8(value)));

    }

    private boolean isRightArgs(int length) {
        return length >= 2 && length % 2 == 0;
    }

}
