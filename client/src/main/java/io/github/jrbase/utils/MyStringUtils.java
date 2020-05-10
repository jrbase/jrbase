package io.github.jrbase.utils;

public class MyStringUtils {
    private MyStringUtils() {

    }
    public static String[] getCmdAndArgs(String msg) {
        return msg.trim().split("\\s+");
    }
}
