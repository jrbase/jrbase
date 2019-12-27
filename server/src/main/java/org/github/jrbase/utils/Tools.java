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

    public static int getByteBit(int position, byte bit) {
        return (0x01 & (bit >> (7 - position)));
    }

    public static int getBit(String position, byte[] bits) {
        try {
            final int i = Integer.parseInt(position);
            return getBit(i, bits);
        } catch (NumberFormatException ignore) {
            return -1;
        }

    }

    public static int getBit(int position, byte[] bits) {
        if (position < 0) {
            return -1;
        }
        if (position >= bits.length * 8) {
            return 0;
        }
        //* 1 byte[] all = new byte[bits.length.*8]
        //*2
        //get bits count
        //   0 1 2 3 4 5 6 7 8
        // a 0 1 1 0 0 0 0 1
        //   0 0 0 0 1        position>>5
        //find certain byte position
        final int bitsIndex = position / 8;
        final int positionByte = (position) % 8;
        return getByteBit(positionByte, bits[bitsIndex]);
    }

}
