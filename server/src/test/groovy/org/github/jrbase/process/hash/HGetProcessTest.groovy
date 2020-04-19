package org.github.jrbase.process.hash

import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.handler.CmdHandler
import org.github.jrbase.process.CmdProcess
import spock.lang.Ignore
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING

@Ignore
class HGetProcessTest extends Specification {
    //hget key field1
    def "Process"() {
        CmdProcess cmdProcess = new HGetProcess()
        ClientCmd clientCmd = new ClientCmd()
        when:
        def chandler = CmdHandler.newSingleInstance(null)
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setKey("a")
        clientCmd.setArgs(["field1"] as String[])
        then:
        REDIS_EMPTY_STRING == cmdProcess.process(clientCmd)
    }

    def "testArgumentsException"() {
        CmdProcess cmdProcess = new HGetProcess()
        ClientCmd clientCmd = new ClientCmd()

        given:
        clientCmd.setKey("key")
        clientCmd.setArgs(["value", "error arg"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }

    def "testArgumentsException22"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs([] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}
