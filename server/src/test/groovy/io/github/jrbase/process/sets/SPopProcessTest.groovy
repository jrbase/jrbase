package io.github.jrbase.process.sets

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

class SPopProcessTest extends Specification {
    private CmdProcess cmdProcess = new SPopProcess()
    @Shared
    private ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        def chandler = CmdHandler.newSingleInstance(null)
        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())

        clientCmd.setKey("a")
    }

    def cleanup() {
    }

    def "processErrorData"() {
        given:

        SAddProcess sAddProcess = new SAddProcess()
        clientCmd.setArgs(["c"] as String[])
        sAddProcess.process(clientCmd)
        expect:
        clientCmd.setArgs(args as String[])
        message == cmdProcess.process(clientCmd)
        where:
        args       | message
        ["1", "2"] | '-ERR syntax error\r\n'
        []         | '*1\r\n$1\r\nc\r\n'
        ["1"]      | '*1\r\n$1\r\nc\r\n'
        ["3"]      | '*1\r\n$1\r\nc\r\n'
        ["a"]      | '-ERR value is not an integer or out of range\r\n'
    }


    def "testArgumentsException"() {
        given:
        clientCmd.setArgs([] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        result
    }

}
