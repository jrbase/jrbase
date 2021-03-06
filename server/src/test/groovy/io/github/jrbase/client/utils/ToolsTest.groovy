package io.github.jrbase.client.utils

import spock.lang.Specification

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


}
