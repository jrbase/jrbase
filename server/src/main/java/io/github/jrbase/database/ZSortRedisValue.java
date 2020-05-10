package io.github.jrbase.database;

import io.github.jrbase.dataType.RedisDataType;
import io.github.jrbase.dataType.ScoreMember;
import io.github.jrbase.utils.skipList.KVPair;
import io.github.jrbase.utils.skipList.SkipList;

import java.util.List;

public class ZSortRedisValue extends RedisValue {

    private final SkipList skipList = new SkipList();

    public ZSortRedisValue() {
        this.setType(RedisDataType.SORTED_SETS);
    }

    public void put(String member, int score) {
        final ScoreMember scoreMember = new ScoreMember(member, score);
        skipList.put(scoreMember);
    }

    public List<KVPair> findRange(int beginIndex, int endIndex) {
        return skipList.findRange(beginIndex, endIndex);
    }

    public int getSize() {
        return skipList.getSize();
    }

}

