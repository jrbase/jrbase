package io.github.jrbase.process.string

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.REDIS_ONE_INTEGER
import static io.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER

class SetBitProcessTest extends Specification {
    CmdProcess cmdProcess = new SetBitProcess()
    @Shared
    ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        def chandler = CmdHandler.newSingleInstance(null)
        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setKey("key")
    }

    def "Process"() {
        expect:
        clientCmd.setArgs(args as String[])
        message == cmdProcess.process(clientCmd)
        where:
        args       | message
        ['1', '1'] | REDIS_ZORE_INTEGER
        ['1', '0'] | REDIS_ONE_INTEGER
        ['2', '0'] | REDIS_ZORE_INTEGER
        ['3', '1'] | REDIS_ZORE_INTEGER
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
