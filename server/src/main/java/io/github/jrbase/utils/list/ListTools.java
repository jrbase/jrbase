package io.github.jrbase.utils.list;

import java.util.ArrayList;
import java.util.List;

import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_LIST;
import static io.github.jrbase.utils.Tools.getRealBegin;

public class ListTools {
    public static String getLRangeList(RedisList list, String begin, String end) {
        final int length = (int) list.getLen();
        int beginInt = Integer.parseInt(begin);
        int endInt = Integer.parseInt(end);
        //2 -1 0 1  length-1
        int realBegin = getRealBegin(length, beginInt);
        int realEnd = getRealBegin(length, endInt);

        if (realBegin > realEnd) {
            return REDIS_EMPTY_LIST;
        } else {
            if (realBegin > length) {
                return REDIS_EMPTY_LIST;
            }
            if (realBegin < 0 && realEnd < 0) {
                return REDIS_EMPTY_LIST;
            }
            realBegin = realBegin < 0 ? 0 : Math.min(realBegin, length - 1);
            realEnd = realEnd < 0 ? 0 : Math.min(realEnd, length - 1);
            return getLRangeList(list, realBegin, realEnd);
        }
    }

    // if begin-0 < list.getLen()-end then query from head else query from tail
    private static String getLRangeList(RedisList list, int begin, int end) {
        List<String> valueList;
        StringBuilder result = new StringBuilder();
        result.append("*").append(end - begin + 1).append("\r\n");
        if (begin <= list.getLen() - 1 - end) {
            valueList = startHead(list, begin, end);
            for (String value : valueList) {
                result.append("$").append(value.length()).append("\r\n").append(value).append("\r\n");
            }
        } else {
            valueList = startTail(list, begin, end);
            for (int i = valueList.size() - 1; i >= 0; i--) {
                String value = valueList.get(i);
                result.append("$").append(value.length()).append("\r\n").append(value).append("\r\n");
            }
        }
        return result.toString();

    }

    private static List<String> startTail(RedisList list, int begin, int end) {
        ListNode search = list.getTail();
        int count = 0;
        while (search != null && count > list.getLen() - 1 - end) {
            search = search.getPrev();
            count++;
        }
        List<String> result = new ArrayList<>();
        while (search != null && count <= list.getLen() - 1 - begin) {
            result.add(search.getValue());
            search = search.getPrev();
            count++;
        }
        return result;
    }

    private static List<String> startHead(RedisList list, int begin, int end) {
        ListNode search = list.getHead();
        int index = 0;
        while (search != null && index > begin) {
            search = search.getNext();
            index++;
        }
        List<String> result = new ArrayList<>();
        while (search != null && index <= end) {
            result.add(search.getValue());
            search = search.getNext();
            index++;
        }
        return result;
    }
}
