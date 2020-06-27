package io.github.jrbase.database;

import io.github.jrbase.client.utils.skipList.SkipList;
import io.github.jrbase.client.utils.skipList.SkipLists;
import io.github.jrbase.common.datatype.RedisDataType;
import io.github.jrbase.dataType.score.PositionScore;

import java.util.List;

public class GeoRedisValue extends RedisValue {

    private final SkipList<PositionScore> skipList = new SkipList<>();

    public GeoRedisValue() {
        this.setType(RedisDataType.GEO);
    }

    public void put(String longitude, String latitude, String member) {
        put(member, Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    public void put(String member, double latitude, double longitude) {
        SkipLists.putPosition(skipList, member, latitude, longitude);
    }

    public List<SkipList<PositionScore>.KVPair> findRange(int beginIndex, int endIndex) {
        return skipList.findRange(beginIndex, endIndex);
    }

    public int getSize() {
        return skipList.getSize();
    }

}

