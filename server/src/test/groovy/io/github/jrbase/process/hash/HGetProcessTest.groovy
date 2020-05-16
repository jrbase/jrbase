package io.github.jrbase.process.hash

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING

class HGetProcessTest extends Specification {

    CmdProcess cmdProcess = new HGetProcess()
    @Shared
    def chandler = CmdHandler.newSingleInstance(null)
    @Shared
    private ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setKey("key")
    }

    //hget key field1
    def "Process"() {
        when:
        clientCmd.setArgs(["field1"] as String[])
        then:
        REDIS_EMPTY_STRING == cmdProcess.process(clientCmd)
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
