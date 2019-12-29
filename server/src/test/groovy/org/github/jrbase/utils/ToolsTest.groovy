package org.github.jrbase.utils


import spock.lang.Specification

import java.nio.charset.StandardCharsets

import static org.github.jrbase.utils.Tools.getBit

class ToolsTest extends Specification {


    def "testIntToByteArray"() {
        expect:
        Tools.intToByteArray(input).length == output

        where:
        input     | output
        1         | 4
        10        | 4
        12345     | 4
        123456789 | 4
    }

    def "testByteArrayToInt"() {
        expect:
        Tools.byteArrayToInt(input as byte[]) == output

        where:
        input        | output
        null         | 0
        [0, 0, 0]    | 0
        [0, 0, 0, 1] | 1
    }

    def "testByteArrayToInt2"() {
        expect:
        Tools.byteArrayToInt(Tools.intToByteArray(input)) == output

        where:
        input     | output
        1         | 1
        10        | 10
        12345     | 12345
        123456789 | 123456789
    }

    def "testStringToByteArray"() {
        expect:
        String.valueOf(input).getBytes().length == output
        where:
        input     | output
        1         | 1
        10        | 2
        12345     | 5
        123456789 | 9

    }

    //get bits count
    //   0 1 2 3 4 5 6 7 8
    //   0 0 0 0 0 0 0 1
    // a 0 1 1 0 0 0 0 1
    //   1
    //                 position<<5   -4
    //                 position<<4   -3
    //                 position<<3   -2
    //                 position<<2   -1
    //                 position<<1   -0


    def "test byte"() {
        expect:
        (0x01 & (97 >> (8 - 1 - position))) == output
        where:
        position | output
        7        | 1
        6        | 0
        5        | 0
        4        | 0
        3        | 0
        2        | 1
        1        | 1
        0        | 0
    }


    def "getbit"() {
        String andy = "abc"
        byte[] bits = andy.getBytes(StandardCharsets.UTF_8)
        //   1 2 3 4 5 6 7 8
        // a 0 1 1 0 0 0 0 1
        //   9 10 11 12 13 14 15 16
        // b 0 1  1  0  0  0  1  0
        // c 0 1  1  0  0  0  1  1
        expect:
        getBit(input, bits) == output
        where:
        input | output
        -1    | -1

        0     | 0
        1     | 1
        2     | 1
        3     | 0
        4     | 0
        5     | 0
        6     | 0
        7     | 1

        8     | 0
        9     | 1
        10    | 1
        11    | 0
        12    | 0
        13    | 0
        14    | 1
        15    | 0

        16    | 0
        17    | 1
        18    | 1
        19    | 0
        20    | 0
        21    | 0
        22    | 1
        23    | 1

        24    | 0

    }


}
