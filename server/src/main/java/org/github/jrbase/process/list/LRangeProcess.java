package org.github.jrbase.process.list;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_LIST;
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER;
import static org.github.jrbase.dataType.RedisDataType.LISTS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;


public class LRangeProcess implements CmdProcess {


    @Override
    public String getCmdName() {
        return Cmd.LRANGE.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() == 2;
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
            return REDIS_EMPTY_LIST;
        } else {     // update list values
            final String resultStr = readUtf8(bGetResult);
            final String[] originValueArr = resultStr.split(REDIS_LIST_DELIMITER);
            return getLRangeList(originValueArr, clientCmd.getArgs()[0], clientCmd.getArgs()[1]);
        }
    }

    // >> > <  == ><  <<
    static String getLRangeList(String[] originValueArr, String begin, String end) {
        final int length = originValueArr.length;
        int beginInt = Integer.parseInt(begin);
        int endInt = Integer.parseInt(end);
        //2 -1 0 1  length-1
        int realBegin = beginInt < 0 ? length + beginInt : beginInt;
        int realEnd = endInt < 0 ? length + endInt : endInt;

        if (realBegin > realEnd) {
            return REDIS_EMPTY_LIST;
        } else {
            // 100     10-1
            if (realBegin > length) {
                return REDIS_EMPTY_LIST;
            }
            if (realBegin < 0 && realEnd < 0) {
                return REDIS_EMPTY_LIST;
            }
            realBegin = realBegin < 0 ? 0 : Math.min(realBegin, length - 1);
            realEnd = realEnd < 0 ? 0 : Math.min(realEnd, length - 1);
            return getLRangeList(originValueArr, realBegin, realEnd);
        }
    }

    private static String getLRangeList(String[] originValueArr, int begin, int end) {
        StringBuilder result = new StringBuilder();
        result.append("*").append(end - begin + 1).append("\r\n");
        for (int i = begin; i <= end; i++) {
            result.append("$").append(originValueArr[i].length()).append("\r\n").append(originValueArr[i]).append("\r\n");
        }
        return result.toString();
    }

}
