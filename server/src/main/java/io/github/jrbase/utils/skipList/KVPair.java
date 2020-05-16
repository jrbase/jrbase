package io.github.jrbase.utils.skipList;

import io.github.jrbase.dataType.ScoreMember;

public class KVPair {
    private final ScoreMember key;
    private final Object value;

    public KVPair(ScoreMember key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object value() {
        return this.value;
    }

    public ScoreMember key() {
        return this.key;
    }

    @Override
    public String toString() {
        return "KVPair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
