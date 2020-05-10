package io.github.jrbase.process.list


import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

class RPushProcessTest extends Specification {
    private CmdProcess cmdProcess = new RPushProcess()

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
        args       | message
        ["a"]      | ':1\r\n'
        ["a", "b"] | ':3\r\n'
        ["a", "b"] | ':5\r\n'
        ["c", "d"] | ':7\r\n'
        ["e", "f"] | ':9\r\n'
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
