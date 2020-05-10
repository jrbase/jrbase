package io.github.jrbase.process.sets


import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER

class SCardProcessTest extends Specification {
    private CmdProcess cmdProcess = new SCardProcess()
    @Shared
    private ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        def chandler = CmdHandler.newSingleInstance(null)
        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())

        clientCmd.setKey("a")
    }

    def "processData"() {
        given:
        def chandler = CmdHandler.newSingleInstance(null)
        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())

        clientCmd.setKey("key")
        SAddProcess sAddProcess = new SAddProcess()

        when:
        clientCmd.setKey("key")
        then:
        REDIS_ZORE_INTEGER == cmdProcess.process(clientCmd)

        when:
        clientCmd.setArgs(["arg1"] as String[])
        sAddProcess.process(clientCmd)
        then:
        ":1\r\n" == cmdProcess.process(clientCmd)

        when:
        clientCmd.setArgs(["arg2"] as String[])
        sAddProcess.process(clientCmd)
        then:
        ":2\r\n" == cmdProcess.process(clientCmd)

    }
}
