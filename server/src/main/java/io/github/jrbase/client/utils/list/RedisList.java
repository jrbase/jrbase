package io.github.jrbase.client.utils.list;

public class RedisList {
    private ListNode head;
    private ListNode tail;
    private long len;

    public ListNode getHead() {
        return head;
    }

    public void setHead(ListNode head) {
        this.head = head;
    }

    public ListNode getTail() {
        return tail;
    }

    public void setTail(ListNode tail) {
        this.tail = tail;
    }

    public long getLen() {
        return len;
    }

    public void setLen(long len) {
        this.len = len;
    }
}
