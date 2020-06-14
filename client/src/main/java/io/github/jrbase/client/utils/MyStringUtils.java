package io.github.jrbase.client.utils;

public class MyStringUtils {
    private MyStringUtils() {

    }
    public static String[] getCmdAndArgs(String msg) {
        return msg.trim().split("\\s+");
    }
}
