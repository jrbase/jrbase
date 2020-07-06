package io.github.jrbase.database;

import io.github.jrbase.client.utils.skipList.SkipList;
import io.github.jrbase.client.utils.skipList.SkipLists;
import io.github.jrbase.common.datatype.RedisDataType;
import io.github.jrbase.dataType.ScoreMember;

import java.util.List;

public class ZSortRedisValue extends RedisValue {

    private final SkipList<ScoreMember> skipList = new SkipList<>();

    public ZSortRedisValue() {
        this.setType(RedisDataType.SORTED_SETS);
    }

    public void put(String member, double score) {
        SkipLists.putScoreMember(skipList, member, score);
    }

    public List<SkipList<ScoreMember>.KVPair> findRange(int beginIndex, int endIndex) {
        return skipList.findRange(beginIndex, endIndex);
    }

    public int getSize() {
        return skipList.getSize();
    }

}

