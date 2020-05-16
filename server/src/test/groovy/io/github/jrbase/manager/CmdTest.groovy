package io.github.jrbase.manager


import io.github.jrbase.process.string.SetProcess
import spock.lang.Specification

class CmdTest extends Specification {

    def "testCmd"() {
        expect:
        input == output
        where:
        input        | output
        'SetProcess' | SetProcess.getSimpleName()
    }

}
