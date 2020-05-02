package org.github.jrbase.utils.skipList;

import org.github.jrbase.dataType.ScoreMember;
import shade.com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.github.jrbase.utils.Tools.getRealBegin;

/**
 * {@link SkipList#insert(ScoreMember, Object)} is most import method. You should care about calculation of span and rank.
 * {@link SkipNode#backward} is used of reversing query.
 * Simple SkipList VS Redis SkipList
 * 1. Simple SkipList
 * Function insert,single query
 * References:
 * https://opendsa-server.cs.vt.edu/ODSA/Books/CS3/html/SkipList.html
 * <p>
 * 2. Redis SkipList
 * Range query: index or score, reverse range query
 * Using HashMap to single query
 * References:
 * http://blog.wjin.org/posts/redis-internal-data-structure-skiplist.html
 * keyword: calculation of span and rank
 * https://blog.csdn.net/idwtwt/article/details/80233859
 * <p>
 * Function: Range Query by SkipList  And Single Query by HashMap
 */
public class SkipList {
    private static final int MAX_LEVEL = 32;
    private final SkipNode head;
    private SkipNode tail;
    // Single Query
    private final Map<String, ScoreMember> hashMap = new ConcurrentHashMap<>();

    public int getSize() {
        return size;
    }

    private int level;
    private int size;
    private static final Random ran = new Random(); // Hold the Random class object

    public SkipList() {
        this.head = new SkipNode(null, null, MAX_LEVEL - 1);
        this.tail = new SkipNode(null, null, MAX_LEVEL - 1);

        this.head.backward = null;
        this.tail = null;
        this.level = 0;
        this.size = 0;
    }

    @VisibleForTesting
    protected SkipNode getHead() {
        return head;
    }


    public int getLevel() {
        return level;
    }

    /**
     * Range query by index, order by the score from high to low
     */
    public List<KVPair> findRevRange(int beginIndex, int endIndex) {
        int begin = getRealBegin(size, beginIndex);
        int end = getRealBegin(size, endIndex);
        if (begin > end) {
            return new ArrayList<>();
        }
        List<KVPair> result = new ArrayList<>(end - begin);
        int index = 0;
        synchronized (this) {
            SkipNode x = tail;
            while (x != this.head) {
                if (index > end) {
                    break;
                }
                if (index < begin) {
                    index++;
                    x = x.backward;
                    continue;
                }
                result.add(x.rec);
                x = x.backward;
                index++;
            }
        }
        return result;
    }

    /**
     * Range query by index, order by the score from low to high
     */
    public List<KVPair> findRange(int beginIndex, int endIndex) {
        int begin = getRealBegin(size, beginIndex);
        int end = getRealBegin(size, endIndex);
        if (begin > end) {
            return new ArrayList<>();
        }
        List<KVPair> result = new ArrayList<>(end - begin);
        SkipNode x = head;
        int index = 0;
        synchronized (this) {
            x = x.level[0].forward;
            while (x != null) {
                if (index > end) {
                    break;
                }
                if (index < begin) {
                    index++;
                    x = x.level[0].forward;
                    continue;
                }
                result.add(x.rec);
                x = x.level[0].forward;
                index++;
            }
        }
        return result;
    }

    public ScoreMember find(ScoreMember key) {
        return this.find(key.getMember());
    }

    public ScoreMember find(String member) {
        return this.hashMap.get(member);
    }

    //TODO: query data by score range
    /*public Object find(int beginScore, int endScore) {
        SkipNode x = head; // Dummy header node
        for (int i = level; i >= 0; i--) { // For each level...
            while ((x.level[i].forward != null) &&
                    (x.level[i].forward.key().compareTo(key) < 0)) { // go forward
                x = x.level[i].forward; // Go one last step
            }
        }
        x = x.level[0].forward; // Move to actual record, if it exists
        if ((x != null) && (x.key().equals(key))) {
            return x.element();
        } else { // Got it
            return null; // Its not there
        }
    }*/


    private int randomLevel() {
        int lev;
        for (lev = 1; Math.abs(ran.nextInt()) % 2 == 0 && lev <= MAX_LEVEL; lev++) { // ran is random generator
            // Do nothing
        }
        return lev;
    }

    /**
     * {@link SkipList#put(ScoreMember)}
     */
    public void put(String member, int score) {
        put(new ScoreMember(member, score));
    }

    /**
     * Insert if absent, update if present
     * {@link SkipList#insert(ScoreMember, Object)}
     *
     * @return insert 1, update 0
     */
    public int put(ScoreMember key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return doPut(key);
    }

    private int doPut(ScoreMember key) {
        synchronized (this) {
            final ScoreMember value = find(key);
            if (value != null) {
                value.setScore(key.getScore());
                return 0;
            } else {
                hashMap.put(key.getMember(), key);
                insert(key, key);
                return 1;
            }
        }
    }

    /**
     * @param key  {@link ScoreMember}
     * @param elem value
     */
    private void insert(ScoreMember key, Object elem) {
        int[] rank = new int[MAX_LEVEL];
        SkipNode[] turnPointArray = new SkipNode[MAX_LEVEL];
        // find turning point then save all level point to turnPointArray
        SkipNode x = head;
        for (int i = level - 1; i >= 0; i--) { // Find insert position
            rank[i] = i == (this.level - 1) ? 0 : rank[i + 1];
            while ((x.level[i].forward != null) &&
                    (x.level[i].forward.key() != null) &&
                    (x.level[i].forward.key().compareTo(key) < 0)) {
                rank[i] += x.level[i].span;
                x = x.level[i].forward;
            }
            turnPointArray[i] = x;
        }
        // generate random and init trunPointArray of `newLevel > this.level`
        int newLevel = randomLevel();
        if (newLevel > this.level) {
            for (int i = this.level; i < newLevel; i++) {
                rank[i] = 0; // point NULL
                turnPointArray[i] = this.head;
                turnPointArray[i].level[i].span = this.size;
            }
            level = newLevel;
        }

        // insert one new Node
        SkipNode insertNode = new SkipNode(key, elem, newLevel);

        for (int i = 0; i < newLevel; i++) {
            insertNode.level[i].forward = turnPointArray[i].level[i].forward;
            turnPointArray[i].level[i].forward = insertNode;
            insertNode.level[i].span = turnPointArray[i].level[i].span - (rank[0] - rank[i]);
            turnPointArray[i].level[i].span = (rank[0] - rank[i] + 1);
        }
        /* increment span for untouched levels */
        for (int i = newLevel; i < this.level; i++) {
            turnPointArray[i].level[i].span++;
        }
        // update backward:
        // turnPointArray ->  insetNode -> insertNode.level[0].forward
        insertNode.backward = turnPointArray[0];
        if (insertNode.level[0].forward != null) {
            insertNode.level[0].forward.backward = insertNode;
        } else {
            //update tail for reverse range query
            this.tail = insertNode;
        }
        // Increment size
        size++;
    }

}
