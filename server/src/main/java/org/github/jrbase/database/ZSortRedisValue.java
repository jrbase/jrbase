package org.github.jrbase.database;

import org.github.jrbase.dataType.RedisDataType;
import org.github.jrbase.dataType.ScoreMember;
import org.github.jrbase.utils.skipList.SkipList;

public class ZSortRedisValue extends RedisValue {

    private final SkipList skipList = new SkipList();

    public ZSortRedisValue() {
        this.setType(RedisDataType.SORTED_SETS);
    }

    public void put(String key, int score) {
        final ScoreMember scoreMember = new ScoreMember(key, score);
        skipList.put(scoreMember);
    }

    public int getSize() {
        return skipList.getSize();
    }

}

