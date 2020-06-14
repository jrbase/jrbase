package io.github.jrbase.client.utils.skipList;

import io.github.jrbase.dataType.ScoreMember;

class ZSkipListLevel {
    public SkipNode forward;
    public int span;

    @Override
    public String toString() {
        return "ZSkipListLevel{" +
                "forward=" + forward +
                ", span=" + span +
                '}';
    }
}

class SkipNode {
    public KVPair rec;
    // number of nodes need be crossed to reach to next node

    // backward pointer, only exist in level zero list
    public SkipNode backward;
    // next node, may skip a lot of nodes
    public ZSkipListLevel[] level;

    public Object element() {
        return rec.value();
    }

    public ScoreMember key() {
        return rec.key();
    }

    SkipNode(ScoreMember key, Object elem, int level) {
        rec = new KVPair(key, elem);
        backward = null;
        this.level = new ZSkipListLevel[level + 1];
        for (int i = 0; i <= level; i++) {
            this.level[i] = new ZSkipListLevel();
        }
    }

    @Override
    public String toString() {
        return "SkipNode{" +
                "rec=" + rec +
                '}';
    }
}
