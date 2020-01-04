package org.github.jrbase.process.sets;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.apache.commons.lang.math.RandomUtils;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;
import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER;
import static org.github.jrbase.dataType.RedisDataType.SETS;
import static org.github.jrbase.utils.Tools.isEmptyBytes;

//spop key [count]

/**
 * Removes and returns one or more random elements from the set value store at key.
 */
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
        if (isEmptyBytes(bGetResult)) {
            return REDIS_EMPTY_STRING;
        } else {
            final String[] args = clientCmd.getArgs();
            if (args.length > 1) {
                return "-ERR syntax error\r\n";
            } else if (args.length == 1) {
                int count;
                try {
                    count = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignore) {
                    //TODO: handle a is number ascii
                    return "-ERR value is not an integer or out of range\r\n";
//
                }
                final String bGetResultStr = readUtf8(bGetResult);
                final String[] getValueArr = bGetResultStr.split(REDIS_LIST_DELIMITER);
                if (count >= getValueArr.length) {
                    //set
                    rheaKVStore.bPut(buildUpKey, writeUtf8(""));
                    //pop
                    return getPopResult(Arrays.asList(getValueArr));
                } else {
                    Set<Integer> randomSets = makeRandomSets(getValueArr.length, count);
                    List<String> popList = new ArrayList<>();
                    List<String> setList = new ArrayList<>();
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
                    //set
                    StringBuilder sPopSetResult = new StringBuilder();
                    for (String s : setList) {
                        sPopSetResult.append(s).append(REDIS_LIST_DELIMITER);
                    }
                    if (sPopSetResult.length() != 0) {
                        sPopSetResult.deleteCharAt(sPopSetResult.length() - 1);
                    }
                    rheaKVStore.bPut(buildUpKey, writeUtf8(sPopSetResult.toString()));
                    //pop
                    return getPopResult(popList);
                }

            } else {
                String sPopResult = "";
                final String bGetResultStr = readUtf8(bGetResult);
                final String[] getValueArr = bGetResultStr.split(REDIS_LIST_DELIMITER);
                final int randomNum = RandomUtils.nextInt(getValueArr.length);
                StringBuilder sPopSetResult = new StringBuilder();
                for (int i = 0; i < getValueArr.length; i++) {
                    if (randomNum == i) {
                        sPopResult = getValueArr[randomNum];
                        continue;
                    }
                    sPopSetResult.append(getValueArr[i]).append(REDIS_LIST_DELIMITER);
                }
                if (sPopSetResult.toString().length() != 0) {
                    sPopSetResult.deleteCharAt(sPopSetResult.length() - 1);
                }
                //bPut
                rheaKVStore.bPut(buildUpKey, writeUtf8(sPopSetResult.toString()));
                return ("*1\r\n$" + sPopResult.length() + "\r\n" + sPopResult + "\r\n");
            }
        }
    }

    @NotNull
    private String getPopResult(List<String> getValueArr) {
        StringBuilder sPopResult = new StringBuilder();
        sPopResult.append("*").append(getValueArr.size()).append("\r\n");
        for (String s : getValueArr) {
            sPopResult.append("$").append(s.length()).append("\r\n").append(s).append("\r\n");
        }
        return sPopResult.toString();
    }


    private Set<Integer> makeRandomSets(int size, int count) {
        Set<Integer> set = new HashSet<>(count);
        while (set.size() >= count) {
            final int randomNum = RandomUtils.nextInt(size);
            set.add(randomNum);
        }
        return set;
    }
}
