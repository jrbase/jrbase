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
    private static final SkipList<ScoreMember> skipList = new SkipList<>();

    @BeforeClass
    public static void init() {
        SkipLists.putScoreMember(skipList, "7", 3);
        SkipLists.putScoreMember(skipList, "a", 5);
        SkipLists.putScoreMember(skipList, "6", 5);
        SkipLists.putScoreMember(skipList, "12", 1);
        SkipLists.putScoreMember(skipList, "3", 13);
        SkipLists.putScoreMember(skipList, "9", 7);
    }

    @Test(expected = NullPointerException.class)
    public void test1PutError() {
        SkipLists.putScoreMember(skipList, null, 0);
    }

    @Test
    public void test2FindRange() {
        //final List<SkipList<ScoreMember>.KVPair> range =
        List<SkipList<ScoreMember>.KVPair> range = skipList.findRange(0, SKIP_LIST_COUNT);
        assertEquals(SKIP_LIST_COUNT, range.size());
        assertEquals("[KVPair{key=12, value=1.0}, KVPair{key=7, value=3.0}, KVPair{key=6, value=5.0}, KVPair{key=a, value=5.0}, KVPair{key=9, value=7.0}, KVPair{key=3, value=13.0}]", range.toString());
    }

    @Test
    public void test3RangeSize() {
        List<SkipList<ScoreMember>.KVPair> range = skipList.findRange(0, -1);
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
        final List<SkipList<ScoreMember>.KVPair> range = skipList.findRevRange(0, SKIP_LIST_COUNT);
        assertEquals(SKIP_LIST_COUNT, range.size());
        assertEquals("[KVPair{key=3, value=13.0}, KVPair{key=9, value=7.0}, KVPair{key=a, value=5.0}, KVPair{key=6, value=5.0}, KVPair{key=7, value=3.0}, KVPair{key=12, value=1.0}]", range.toString());
    }


    @Test
    public void test5Span() {
        SkipList<ScoreMember>.SkipNode next = skipList.getHead();
        while (next.level.get(0).forward != null) {
            System.out.println("level:" + next.level.get(0));
            assertEquals(1, next.level.get(0).span);
            next = next.level.get(0).forward;
        }
    }

    @Test
    public void test6Span2() {
        final SkipList<ScoreMember>.SkipNode head = skipList.getHead();
        for (int i = 0; i <= skipList.getLevel(); i++) {
            SkipList<ScoreMember>.SkipNode next = head;
            while (next.level.get(i).forward != null) {
                System.out.println("level:" + i + next.level.get(i));
                assertTrue(next.level.get(i).span > 0);
                next = next.level.get(i).forward;
            }
            System.out.println("------------------");
        }
    }


    @Test
    public void test7Update() {
        SkipLists.putScoreMember(skipList, "7", 100);
        ScoreMember newValue = skipList.find("7");
        assertEquals(100f, newValue.getScore(), 4);
        SkipLists.putScoreMember(skipList, "7", 3);
        newValue = skipList.find("7");
        assertEquals(3, newValue.getScore(), 4);
    }

    @Test
    public void test8MultiThread2PutSame() throws InterruptedException {
        Object lock = new Object();
        int threadCount = 10;
        CountDownLatch downLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    SkipLists.putScoreMember(skipList, "pre" + j, j);
                }
                downLatch.countDown();
            }).start();
        }
        downLatch.await();
        assertEquals(100 + SKIP_LIST_COUNT, skipList.getSize());
    }

}
