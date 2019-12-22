package org.github.jrbase.utils;

public class Tools {
    public static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value};
    }

    public static int byteArrayToInt(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                ((bytes[3] & 0xFF));
    }

    public static boolean isRightArgs(int expectLength, int exactLength) {
        return expectLength == exactLength;
    }

}
