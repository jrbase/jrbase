package io.github.jrbase.client.utils.skipList;

import io.github.jrbase.dataType.ScoreMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

import static io.github.jrbase.client.utils.Tools.getRealBegin;

//import shade.com.google.common.annotations.VisibleForTesting;

/**
 * {@link SkipList#(ScoreMember, Object)} is most import method. You should care about calculation of span and rank.
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
public class SkipList<T extends Comparable<T>> {
    private final Lock lock = new ReentrantLock();

    private static final int MAX_LEVEL = 32;
    private final SkipNode head;
    private SkipNode tail;
    // Single Query
    private final Map<String, T> hashMap = new ConcurrentHashMap<>();

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

    //    @VisibleForTesting
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
            x = x.level.get(0).forward;
            while (x != null) {
                if (index > end) {
                    break;
                }
                if (index < begin) {
                    index++;
                    x = x.level.get(0).forward;
                    continue;
                }
                result.add(x.rec);
                x = x.level.get(0).forward;
                index++;
            }
        }
        return result;
    }

    public T find(String member) {
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

    void put(String key, T elem) {
        hashMap.put(key, elem);
        insert(key, elem);
    }


    /**
     * @param key  {@link ScoreMember}
     * @param elem value
     */
    private void insert(String key, T elem) {
        int[] rank = new int[MAX_LEVEL];
        List<SkipNode> turnPointArray = new ArrayList<>(MAX_LEVEL);
        for (int f = 0; f < MAX_LEVEL; f++) {
            turnPointArray.add(null);
        }
        // find turning point then save all level point to turnPointArray
        SkipNode x = head;
        for (int i = level - 1; i >= 0; i--) { // Find insert position
            rank[i] = i == (this.level - 1) ? 0 : rank[i + 1];
            while ((x.level.get(i).forward != null) &&
                    (x.level.get(i).forward.key() != null) &&
                    compareKeyAndScore(key, x, i)) {
                rank[i] += x.level.get(i).span;
                x = x.level.get(i).forward;
            }
            turnPointArray.set(i, x);
        }
        // generate random and init trunPointArray of `newLevel > this.level`
        int newLevel = randomLevel();
        if (newLevel > this.level) {
            for (int i = this.level; i < newLevel; i++) {
                rank[i] = 0; // point NULL
                turnPointArray.set(i, this.head);
                turnPointArray.get(i).level.get(i).span = this.size;
            }
            level = newLevel;
        }

        // insert one new Node
        SkipNode insertNode = new SkipNode(key, elem, newLevel);

        for (int i = 0; i < newLevel; i++) {
            insertNode.level.get(i).forward = turnPointArray.get(i).level.get(i).forward;
            turnPointArray.get(i).level.get(i).forward = insertNode;
            insertNode.level.get(i).span = turnPointArray.get(i).level.get(i).span - (rank[0] - rank[i]);
            turnPointArray.get(i).level.get(i).span = (rank[0] - rank[i] + 1);
        }
        /* increment span for untouched levels */
        for (int i = newLevel; i < this.level; i++) {
            turnPointArray.get(i).level.get(i).span++;
        }
        // update backward:
        // turnPointArray ->  insetNode -> insertNode.level[0].forward
        insertNode.backward = turnPointArray.get(0);
        if (insertNode.level.get(0).forward != null) {
            insertNode.level.get(0).forward.backward = insertNode;
        } else {
            //update tail for reverse range query
            this.tail = insertNode;
        }
        // Increment size
        size++;
    }

    private boolean compareKeyAndScore(String key, SkipNode x, int i) {
        T v1 = hashMap.get(x.level.get(i).forward.key());
        T v2 = hashMap.get(key);
        int result = compare(v1, v2, Comparable::compareTo);
        if (result < 0) {
            return true;
        } else if (result > 0) {
            return false;
        } else {

            return x.level.get(i).forward.key().compareTo(key) < 0;
        }

    }

    private int compare(T a, T b, BiFunction<T, T, Integer> biFunction) {
        return biFunction.apply(a, b);
    }

    public void lock() {
        lock.lock();
    }

    public void unLock() {
        lock.unlock();
    }

    public class KVPair {
        private final String key;
        private final T value;

        public KVPair(String key, T value) {
            this.key = key;
            this.value = value;
        }

        public T value() {
            return this.value;
        }

        public String key() {
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

    public class SkipNode {
        public KVPair rec;
        // number of nodes need be crossed to reach to next node

        // backward pointer, only exist in level zero list
        public SkipNode backward;
        // next node, may skip a lot of nodes
        public List<ZSkipListLevel> level;

        SkipNode(String key, T elem, int level) {
            rec = new KVPair(key, elem);
            backward = null;
            this.level = new ArrayList<>();
            for (int i = 0; i <= level; i++) {
                this.level.add(new ZSkipListLevel());
            }
        }

        public Object element() {
            return rec.value();
        }

        public String key() {
            return rec.key();
        }

        @Override
        public String toString() {
            return "SkipNode{" +
                    "rec=" + rec +
                    '}';
        }
    }

}
