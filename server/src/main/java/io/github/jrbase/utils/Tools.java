package io.github.jrbase.utils;

public class Tools {
    public static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value};
    }

    public static int byteArrayToInt(byte[] bytes) {
        if (bytes == null || bytes.length != 4) {
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

    public static boolean checkArgs(int expectLength, int exactLength) {
        return expectLength == exactLength;
    }

    public static boolean isCorrectKey(String key) {
        return !key.isEmpty();
    }


    public static int getBit(String position, byte[] bits) {
        try {
            final int positionInt = Integer.parseInt(position);
            return getBit(positionInt, bits);
        } catch (NumberFormatException ignore) {
            return -1;
        }

    }

    public static int getBit(int position, byte[] bits) {
        return bits[position];
    }

    public static byte[] setBit(int position, int value, byte[] bits) {
        final byte result = bits[position];
        bits[position] = (byte) (value & 0xff);
        return bits;
    }

    public static int getRealBegin(int length, int beginInt) {
        return beginInt < 0 ? length + beginInt : beginInt;
    }

    public static int getRealBegin(long length, long beginInt) {
        return (int) (beginInt < 0 ? length + beginInt : beginInt);
    }

}
