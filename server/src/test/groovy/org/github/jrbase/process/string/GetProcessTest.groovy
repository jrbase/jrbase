package org.github.jrbase.process.string

import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.handler.CmdHandler
import org.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING

class GetProcessTest extends Specification {
    CmdProcess cmdProcess = new GetProcess()
    @Shared
    def chandler = CmdHandler.newSingleInstance(null)
    @Shared
    private ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        chandler.getDefaultDB().getTable().clear()
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setKey("key")
    }

    //get a = b
    def "Process"() {
        when:
        clientCmd.setArgs("a")
        then:
        REDIS_EMPTY_STRING == cmdProcess.process(clientCmd)
    }

    def "set and get "() {
        CmdProcess setProcess = new SetProcess()
        clientCmd.setArgs(["value1"] as String[])
        when:
        setProcess.process(clientCmd)
        then:
        '$6\r\nvalue1\r\n' == cmdProcess.process(clientCmd)
    }

    def "testArgumentsException"() {
        given:
        clientCmd.setArgs(["value", "error arg"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }

}
