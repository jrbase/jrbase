package io.github.jrbase.client.response;

import java.util.HashMap;
import java.util.Map;

public class ResponseFactory {
    private static final Map<String, TypeResponse> map = new HashMap<>();

    static {
        map.put("+", new SimpleStringsResponse());
        map.put("-", new ErrorsResponse());
        map.put(":", new IntegersResponse());
        map.put("$", new BulkStringsResponse());
        map.put("*", new ArraysResponse());
        map.put("other", new OtherResponse());
    }

    public static TypeResponse selectTypeResponse(String msg) {
        String specialChar = msg.substring(0, 1);
        TypeResponse typeResponse = map.get(specialChar);
        if (typeResponse == null) {
            return map.get("other");
        } else {
            return typeResponse;
        }

    }
}
