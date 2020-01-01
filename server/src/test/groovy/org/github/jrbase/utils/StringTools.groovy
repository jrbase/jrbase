package org.github.jrbase.utils

import spock.lang.Specification

class StringTools extends Specification {

    def "test StringBuilder"() {
        when:
        StringBuilder buildUpValue = new StringBuilder()
        buildUpValue.append("1").append(",")
        buildUpValue.deleteCharAt(buildUpValue.length() - 1)
        then:
        buildUpValue.toString() == "1"
    }
}
