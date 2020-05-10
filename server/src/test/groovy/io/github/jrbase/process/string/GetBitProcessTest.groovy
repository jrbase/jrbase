package io.github.jrbase.process.string

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

class GetBitProcessTest extends Specification {
    CmdProcess cmdProcess = new GetBitProcess()
    @Shared
    ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        def chandler = CmdHandler.newSingleInstance(null)
        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setKey("key")
        //

    }

    def "Process"() {
        SetBitProcess setBitProcess = new SetBitProcess()
        clientCmd.setArgs(["1", "1"] as String[])
        setBitProcess.process(clientCmd)
        clientCmd.setArgs(["2", "1"] as String[])
        setBitProcess.process(clientCmd)
        clientCmd.setArgs(["3", "0"] as String[])
        setBitProcess.process(clientCmd)

        clientCmd.setArgs(args as String[])

        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args  | message
        ['5'] | ':0\r\n'
        ['1'] | ':1\r\n'
        ['2'] | ':1\r\n'
        ['3'] | ':0\r\n'
    }


    def "testArgumentsException"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs([] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}
