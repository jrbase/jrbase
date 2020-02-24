package org.github.jrbase.process.sets;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.apache.commons.lang.math.RandomUtils;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.KeyCommand;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER;
import static org.github.jrbase.dataType.RedisDataType.SETS;
import static org.github.jrbase.utils.ToolsString.deleteLastChar;

/**
 * spop key [count]
 * Removes and returns one or more random elements from the set value store at key.
 */
@KeyCommand
public class SPopProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return Cmd.SPOP.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return true;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        final RheaKVStore rheaKVStore = clientCmd.getRheaKVStore();

        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation();
        //bGet
        final byte[] bGetResult = rheaKVStore.bGet(buildUpKey);
        final String bGetResultStr = readUtf8(bGetResult);
        //1 no key
        if (isEmpty(bGetResultStr)) {
            return REDIS_EMPTY_STRING;
        } else {
            final String[] args = clientCmd.getArgs();
            //2 error
            if (args.length > 1) {
                return "-ERR syntax error\r\n";
            } else if (args.length == 1) {  //3 have count argument
                return handleCountArgument(rheaKVStore, buildUpKey, bGetResultStr, args);
            } else {// 4 have no count argument
                return handlePopOneSet(rheaKVStore, buildUpKey, bGetResultStr);
            }
        }
    }


    @NotNull
    private String handlePopOneSet(RheaKVStore rheaKVStore, String buildUpKey, String bGetResultStr) {
        final String[] getValueArr = bGetResultStr.split(REDIS_LIST_DELIMITER);
        final int randomNum = RandomUtils.nextInt(getValueArr.length);
        StringBuilder sPopSetResult = getPopUpdateResult(getValueArr, randomNum);
        //set
        rheaKVStore.bPut(buildUpKey, writeUtf8(sPopSetResult.toString()));
        //pop
        String randomPopValue = GetRandomPopValue(getValueArr, randomNum);
        return ("*1\r\n$" + randomPopValue.length() + "\r\n" + randomPopValue + "\r\n");
    }

    @NotNull
    private String handleCountArgument(RheaKVStore rheaKVStore, String buildUpKey, String bGetResultStr, String[] args) {
        int count;
        try {
            count = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignore) {
            return "-ERR value is not an integer or out of range\r\n";
        }

        final String[] getValueArr = bGetResultStr.split(REDIS_LIST_DELIMITER);
        // 5 return all data
        if (count >= getValueArr.length) {
            //set
            rheaKVStore.bPut(buildUpKey, writeUtf8(""));
            //pop
            return getPopResult(Arrays.asList(getValueArr));
        } else {//6 random return count values
            Set<Integer> randomSets = makeRandomSets(getValueArr.length, count);
            final List<String> popList = new ArrayList<>();
            final List<String> updateList = new ArrayList<>();
            getPopListAndUpdateList(getValueArr, randomSets, popList, updateList);
            //set
            final StringBuilder updateResult = getUpdateData(updateList);
            rheaKVStore.bPut(buildUpKey, writeUtf8(updateResult.toString()));
            //pop
            return getPopResult(popList);
        }
    }

    private StringBuilder getUpdateData(final List<String> updateList) {
        StringBuilder sPopSetResult = new StringBuilder();
        for (String s : updateList) {
            sPopSetResult.append(s).append(REDIS_LIST_DELIMITER);
        }
        deleteLastChar(sPopSetResult);
        return sPopSetResult;
    }

    private String GetRandomPopValue(String[] getValueArr, int randomNum) {
        return getValueArr[randomNum];
    }


    @NotNull
    private StringBuilder getPopUpdateResult(String[] getValueArr, int randomNum) {
        StringBuilder sPopSetResult = new StringBuilder();
        for (int i = 0; i < getValueArr.length; i++) {
            if (randomNum == i) {
                continue;
            }
            sPopSetResult.append(getValueArr[i]).append(REDIS_LIST_DELIMITER);
        }
        deleteLastChar(sPopSetResult);
        return sPopSetResult;
    }


    private void getPopListAndUpdateList(final String[] getValueArr, final Set<Integer> randomSets, final List<String> popList, final List<String> setList) {
        for (int i = 0; i < getValueArr.length; i++) {
            boolean addPop = false;
            for (int randomNum : randomSets) {
                if (randomNum == i) {
                    addPop = true;
                    break;
                }
            }
            if (addPop) {
                popList.add(getValueArr[i]);
            } else {
                setList.add(getValueArr[i]);
            }
        }
    }

    @NotNull
    String getPopResult(final List<String> getValueArr) {
        StringBuilder sPopResult = new StringBuilder();
        sPopResult.append("*").append(getValueArr.size()).append("\r\n");
        for (String s : getValueArr) {
            sPopResult.append("$").append(s.length()).append("\r\n").append(s).append("\r\n");
        }
        return sPopResult.toString();
    }

    Set<Integer> makeRandomSets(int size, int count) {
        Set<Integer> set = new HashSet<>(count);
        while (set.size() < count) {
            final int randomNum = RandomUtils.nextInt(size);
            set.add(randomNum);
        }
        return set;
    }
}
