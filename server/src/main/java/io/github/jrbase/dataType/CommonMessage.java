package io.github.jrbase.dataType;

public class CommonMessage {
    //    public static final String REDiS_STRING_EMPTY = "";
    public static final String REDIS_ERROR_SYNTAX = "-ERR syntax error\r\n";
    public static final String REDIS_ERROR_INTEGER_OUT_RANGE = "-ERR value is not an integer or out of range\r\n";
    public static final String REDIS_ERROR_OPERATION_AGAINST = "-WRONGTYPE Operation against a key holding the wrong kind of value\r\n";
    public static final String REDIS_ZORE_INTEGER = ":0\r\n";
    public static final String REDIS_ONE_INTEGER = ":1\r\n";
    public static final String REDIS_EMPTY_STRING = "$-1\r\n";
    public static final String REDIS_LIST_DELIMITER = ",";
    public static final String REDIS_EMPTY_LIST = "+(empty list or set)\r\n";
    public static final String REDIS_EMPTY_SET = "+(empty list or set)\r\n";

}
