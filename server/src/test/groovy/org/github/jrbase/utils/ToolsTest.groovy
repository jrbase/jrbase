package org.github.jrbase.utils

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

}
