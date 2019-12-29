package org.github.jrbase.utils;

import org.github.jrbase.execption.ArgumentsException;

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

    public static void checkArgs(int expectLength, int exactLength) throws ArgumentsException {
        if (expectLength != exactLength) {
            throw new ArgumentsException();
        }
    }

    public static void checkKey(String key) throws ArgumentsException {
        if (key.isEmpty()) {
            throw new ArgumentsException();
        }
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


    public static int getByteBit(int position, byte bit) {
        return (0x01 & (bit >> (7 - position)));
    }

    // value 1 0
    public static int setBit(String position, String value, byte[] bits) {
        try {
            int positionInt = Integer.parseInt(position);
            int valueInt = Integer.parseInt(value);
            return setBit(positionInt, valueInt, bits);
        } catch (NumberFormatException ignore) {
            return -1;
        }

    }

    // value 1 0
    public static int setBit(int position, int value, byte[] bits) {
        if (position < 0) {
            return -1;
        }
        if (position >= bits.length * 8) {
            return 0;
        }

        final int bitsIndex = position / 8;
        final int positionByte = (position) % 8;
        //get bits count
        //   0 1 2 3 4 5 6 7 8
        // a 0 1 1 0 0 0 0 1
        //   0 1 0 0 0 0 0 0
        //~  1 0 1 1 1 1 1 1
        // & a 0 1 1 0 0 0 0 1
        // = 1 0 1 0 0 0 0 1
//------------
        // a 0 1 1 0 0 0 0 1
        // | 0 0 0 0 0 0 1 0
        // = 1 0 0 0 0 0 0 1

        //1 0   0
        //0 0   0
        if (value == 0) {
            int updateByte = bits[bitsIndex] & ~(0x01 << (7 - positionByte));
            bits[bitsIndex] = (byte) updateByte;
        } else {
            int updateByte = bits[bitsIndex] | (0x01 << (7 - positionByte));
            bits[bitsIndex] = (byte) updateByte;
        }
        return 0;
        // |
        //1 1   1
        //0 1   1
        //1 0   1
        //0 0   0
    }


}
