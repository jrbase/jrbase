package org.github.jrbase.utils

import spock.lang.Specification

import static org.github.jrbase.utils.ToolsString.deleteLastChar

class StringTools extends Specification {

    def "test StringBuilder"() {
        when:
        StringBuilder buildUpValue = new StringBuilder()
        buildUpValue.append("1").append(",")
        deleteLastChar(buildUpValue)
        then:
        buildUpValue.toString() == "1"
    }
}
