package org.github.jrbase.process.string

import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.handler.CmdHandler
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER

class SetProcessTest extends Specification {
    private CmdProcess cmdProcess = new SetProcess()
    private ClientCmd clientCmd = new ClientCmd()

    def "Process"() {
        given:
        def chandler = CmdHandler.newSingleInstance(null)
        clientCmd.setDb(chandler.getDefaultDB())

        clientCmd.setKey("key")
        String[] arr = args as String[]
        clientCmd.setArgs(arr)
        //set key field value
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args      | message
        ["value"] | ':1\r\n'
        ["value"] | REDIS_ZORE_INTEGER
    }

    def "testArgumentsException"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs(["value", "error arg"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }

}
