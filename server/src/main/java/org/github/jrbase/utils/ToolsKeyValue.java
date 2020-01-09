package org.github.jrbase.utils;

import java.util.HashMap;
import java.util.Map;

public class ToolsKeyValue {

    public static boolean keyValueEventNumber(int length) {
        return length >= 2 && length % 2 == 0;
    }

    // cmd key field value
    public static Map<String, String> generateKeyValueMap(String[] args) {
        Map<String, String> keyValueMap = new HashMap<>();
        for (int i = 0; i < args.length; i = i + 2) {
            keyValueMap.put(args[i], args[i + 1]);
        }
        return keyValueMap;
    }

}
