package io.github.jrbase.database;

import io.github.jrbase.dataType.RedisDataType;
import io.github.jrbase.utils.list.ListNode;
import io.github.jrbase.utils.list.RedisList;

import static io.github.jrbase.utils.list.ListTools.getLRangeList;

public class ListRedisValue extends RedisValue {

    private final RedisList list = new RedisList();

    public ListRedisValue() {
        this.setType(RedisDataType.LISTS);
    }

    public void addLast(ListNode node) {
        if (null == list.getTail()) {
            list.setHead(node);
            list.setTail(node);
        } else {
            final ListNode tail = list.getTail();
            tail.setNext(node);
            list.setTail(node);
            node.setPrev(tail);
        }
        list.setLen(list.getLen() + 1);
    }

    public void addFirst(ListNode node) {
        if (null == list.getHead()) {
            list.setHead(node);
            list.setTail(node);
        } else {
            final ListNode curHead = list.getHead();
            curHead.setPrev(node);
            list.setHead(node);
            node.setNext(curHead);
        }
        list.setLen(list.getLen() + 1);
    }

    public long getSize() {
        return list.getLen();
    }

    public ListNode popLast() {
        if (list.getTail() == null) {
            return null;
        }
        final ListNode pop = list.getTail();
        list.setTail(list.getTail().getPrev());
        if (list.getTail() != null) {
            list.getTail().setNext(null);
        }
        list.setLen(list.getLen() - 1);
        return pop;
    }

    // 0 1 2 3
    public ListNode popFirst() {
        if (list.getHead() == null) {
            return null;
        }
        final ListNode pop = list.getHead();
        list.setHead(list.getHead().getNext());
        if (list.getHead() != null) {
            list.getHead().setPrev(null);
        }
        list.setLen(list.getLen() - 1);
        return pop;
    }

    public String findRange(String begin, String end) {
        return getLRangeList(list, begin, end);
    }

}

