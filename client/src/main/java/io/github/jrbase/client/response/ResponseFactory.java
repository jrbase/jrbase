package io.github.jrbase.client.response;

public class ResponseFactory {
    public static TypeResponse newTypeResponse(String msg) {
        if (msg.startsWith("+")) {
            return new SimpleStringsResponse();
        } else if(msg.startsWith("-ERR")) {
            return new ErrorsResponse();
        } else if(msg.startsWith(":")) {
            return new IntegersResponse();
        } else if(msg.startsWith("$")) {
            return new BulkStringsResponse();
        }else if(msg.startsWith("*")) {
            return new ArraysResponse();
        }else {
            return new OtherResponse();
        }

    }
}
