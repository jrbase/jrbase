package io.github.jrbase.database;

import io.github.jrbase.utils.list.ListNode;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListRedisValueTest {
    ListRedisValue listRedisValue = new ListRedisValue();

    @BeforeClass
    public static void init() {

    }

    @Test
    public void test0AddLast() {
        //-2 0000 1111 3333
        listRedisValue.addLast(new ListNode("1111"));
        assertEquals(1, listRedisValue.getSize());

        listRedisValue.addFirst(new ListNode("0000"));
        assertEquals(2, listRedisValue.getSize());

        listRedisValue.addLast(new ListNode("3333"));
        assertEquals(3, listRedisValue.getSize());

        listRedisValue.addFirst(new ListNode("-2"));
        assertEquals(4, listRedisValue.getSize());

        ListNode listNode = listRedisValue.popLast();
        assertEquals(3, listRedisValue.getSize());
        assertEquals("3333", listNode.getValue());

        listNode = listRedisValue.popFirst();
        assertEquals(2, listRedisValue.getSize());
        assertEquals("-2", listNode.getValue());

        listNode = listRedisValue.popLast();
        assertEquals(1, listRedisValue.getSize());
        assertEquals("1111", listNode.getValue());

        listNode = listRedisValue.popFirst();
        assertEquals(0, listRedisValue.getSize());
        assertEquals("0000", listNode.getValue());
    }

}