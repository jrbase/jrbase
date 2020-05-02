package org.github.jrbase.process.zsets

import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.handler.CmdHandler
import org.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

class ZAddProcessTest extends Specification {
    private CmdProcess cmdProcess = new ZAddProcess()
    @Shared
    def chandler = CmdHandler.newSingleInstance(null)
    @Shared
    private ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        chandler.getDefaultDB().getTable().clear()
        clientCmd.setKey("a")
    }

    def cleanup() {
    }

    def "processData"() {
        given:
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setArgs(args as String[])

        expect:
        cmdProcess.process(clientCmd) == message
        where:
        args                 | message
        ["1", "a"]           | ':1\r\n'
        ["1", "b"]           | ':1\r\n'
        ["1", "b"]           | ':0\r\n'
        ["2", "b"]           | ':0\r\n'
        ["1", "b", "2", "c"] | ':1\r\n'
        ["1", "b", "2", "c"] | ':0\r\n'
        ["1", "b", "3", "c"] | ':0\r\n'
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
