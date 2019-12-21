package org.github.jrbase.manager

import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.SetProcess
import spock.lang.Specification

class CmdTest extends Specification {

    def "testCmd"() {
        expect:
        input == output
        where:
        input | output
        'SetProcess' | SetProcess.getSimpleName()
    }

}
