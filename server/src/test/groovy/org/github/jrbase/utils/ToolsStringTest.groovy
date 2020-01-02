package org.github.jrbase.utils


import spock.lang.Specification

class ToolsStringTest extends Specification {

    def setup() {
    }

    def "GetLeftBuildUpArgsValue"() {
        expect:
        result == ToolsString.getLeftBuildUpArgsValue(args as String[]).toString()
        where:
        result  | args
        ""      | []
        "a"     | ["a",]
        "b,a"   | ["a", "b"]
        "c,b,a" | ["a", "b", "c"]
    }

    def "GetLeftBuildUpValue"() {
        expect:
        result == ToolsString.getLeftBuildUpValue(args as String[], originValueArr as String[])
        where:
        result      | args            | originValueArr
        ""          | []              | []
        "a"         | ["a"]           | []
        "a,e,f"     | ["a"]           | ["e", "f"]
        "b,a,e,f"   | ["a", "b"]      | ["e", "f"]
        "c,b,a,e,f" | ["a", "b", "c"] | ["e", "f"]
    }

    def "GetLPopBuildUpValue"() {
        expect:
        result == ToolsString.getLPopBuildUpValue(originValueArr as String[])
        where:
        result | originValueArr
        ""     | []
        ""     | ["e"]
        "f"    | ["e", "f"]
        "f,g"  | ["e", "f", "g"]
    }

    def "GetRightBuildUpArgsValue"() {
        expect:
        result == ToolsString.getRightBuildUpArgsValue(args as String[]).toString()
        where:
        result  | args
        ""      | []
        "a"     | ["a",]
        "a,b"   | ["a", "b"]
        "a,b,c" | ["a", "b", "c"]
    }

    def "GetRightBuildUpValue"() {
        expect:
        result == ToolsString.getRightBuildUpValue(args as String[], originValueArr as String[])
        where:
        result      | args            | originValueArr
        ""          | []              | []
        "a"         | ["a"]           | []
        "e,f,a"     | ["a"]           | ["e", "f"]
        "e,f,a,b"   | ["a", "b"]      | ["e", "f"]
        "e,f,a,b,c" | ["a", "b", "c"] | ["e", "f"]
    }

    def "GetRPopBuildUpValue"() {
        expect:
        result == ToolsString.getRPopBuildUpValue(originValueArr as String[])
        where:
        result | originValueArr
        ""     | []
        ""     | ["e"]
        "e"    | ["e", "f"]
        "e,f"  | ["e", "f", "g"]
    }
}
