package io.github.jrbase.process.sets


import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

class SAddProcessTest extends Specification {

    private CmdProcess cmdProcess = new SAddProcess()
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

    def "processData"() {
        given:
        clientCmd.setArgs(args as String[])
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args            | message
        ["1"]           | ':1\r\n'
        ["a", "b"]      | ':2\r\n'
        ["a", "b"]      | ':0\r\n'
        ["a", "a"]      | ':0\r\n'
        ["a", "b", "c"] | ':1\r\n'
        ["d", "e", "f"] | ':3\r\n'
    }

    def "testArgumentsException"() {
        given:
        clientCmd.setArgs([] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}
