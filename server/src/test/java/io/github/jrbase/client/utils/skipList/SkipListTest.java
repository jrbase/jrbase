package io.github.jrbase.client.utils.skipList;

import io.github.jrbase.dataType.ScoreMember;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SkipListTest {

    private static final int SKIP_LIST_COUNT = 6;
    private static final SkipList skipList = new SkipList();

    @BeforeClass
    public static void init() {
        skipList.put(new ScoreMember("7", 3));
        skipList.put(new ScoreMember("6", 5));
        skipList.put(new ScoreMember("a", 8));
        skipList.put(new ScoreMember("12", 1));
        skipList.put(new ScoreMember("3", 13));
        skipList.put(new ScoreMember("9", 7));
    }

    @Test(expected = NullPointerException.class)
    public void test1PutError() {
        skipList.put(null);
    }


    @Test
    public void test2FindRange() {
        final List<KVPair> range = skipList.findRange(0, SKIP_LIST_COUNT);
        assertEquals(SKIP_LIST_COUNT, range.size());
        assertEquals("[KVPair{key=ScoreMember{member='12', score=1}, value=ScoreMember{member='12', score=1}}, KVPair{key=ScoreMember{member='7', score=3}, value=ScoreMember{member='7', score=3}}, KVPair{key=ScoreMember{member='6', score=5}, value=ScoreMember{member='6', score=5}}, KVPair{key=ScoreMember{member='9', score=7}, value=ScoreMember{member='9', score=7}}, KVPair{key=ScoreMember{member='a', score=8}, value=ScoreMember{member='a', score=8}}, KVPair{key=ScoreMember{member='3', score=13}, value=ScoreMember{member='3', score=13}}]",
                range.toString());
    }

    @Test
    public void test3RangeSize() {
        List<KVPair> range = skipList.findRange(0, -1);
        assertEquals(SKIP_LIST_COUNT, range.size());

        range = skipList.findRange(0, 0);
        assertEquals(1, range.size());

        range = skipList.findRange(-2, -1);
        assertEquals(2, range.size());

        range = skipList.findRange(0, 100);
        assertEquals(SKIP_LIST_COUNT, range.size());

        range = skipList.findRange(100, 0);
        assertEquals(0, range.size());
    }

    @Test
    public void test4FindRevRange() {
        final List<KVPair> range = skipList.findRevRange(0, SKIP_LIST_COUNT);
        assertEquals(SKIP_LIST_COUNT, range.size());
        assertEquals("[KVPair{key=ScoreMember{member='3', score=13}, value=ScoreMember{member='3', score=13}}, KVPair{key=ScoreMember{member='a', score=8}, value=ScoreMember{member='a', score=8}}, KVPair{key=ScoreMember{member='9', score=7}, value=ScoreMember{member='9', score=7}}, KVPair{key=ScoreMember{member='6', score=5}, value=ScoreMember{member='6', score=5}}, KVPair{key=ScoreMember{member='7', score=3}, value=ScoreMember{member='7', score=3}}, KVPair{key=ScoreMember{member='12', score=1}, value=ScoreMember{member='12', score=1}}]",
                range.toString());
    }


    @Test
    public void test5Span() {
        SkipNode next = skipList.getHead();
        while (next.level[0].forward != null) {
            System.out.println("level:" + next.level[0]);
            assertEquals(1, next.level[0].span);
            next = next.level[0].forward;
        }
    }

    @Test
    public void test6Span2() {
        final SkipNode head = skipList.getHead();
        for (int i = 0; i <= skipList.getLevel(); i++) {
            SkipNode next = head;
            while (next.level[i].forward != null) {
                System.out.println("level:" + i + next.level[i]);
                assertTrue(next.level[0].span > 0);
                next = next.level[i].forward;
            }
            System.out.println("------------------");
        }
    }


    @Test
    public void test7Update() {
        skipList.put(new ScoreMember("7", 100));
        ScoreMember newValue = skipList.find(new ScoreMember("7", 100));
        assertEquals(100, newValue.getScore());
        skipList.put("7", 3);
        newValue = skipList.find("7");
        assertEquals(3, newValue.getScore());
    }

    @Test
    public void test8MultiThread2PutSame() throws InterruptedException {
        int threadCount = 10;
        CountDownLatch downLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    skipList.put(new ScoreMember(("pre" + j), j));
                }
                downLatch.countDown();
            }).start();
        }
        downLatch.await();
        assertEquals(100 + SKIP_LIST_COUNT, skipList.getSize());
    }

}
