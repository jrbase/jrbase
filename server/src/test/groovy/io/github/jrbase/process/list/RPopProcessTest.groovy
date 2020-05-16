package io.github.jrbase.process.list

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING

class RPopProcessTest extends Specification {
    private CmdProcess cmdProcess = new RPopProcess()
    private ClientCmd clientCmd = new ClientCmd()

    def setup() {
        def chandler = CmdHandler.newSingleInstance(null)
        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setKey("a")

        RPushProcess rPushProcess = new RPushProcess()
        clientCmd.setArgs(["abc", "b", "c"] as String[])
        rPushProcess.process(clientCmd)
        clientCmd.setArgs([] as String[])
        // abc b c
    }

    def "processData"() {
        when:
        def message = cmdProcess.process(clientCmd)
        then:
        message == '$1\r\nc\r\n'

        when:
        message = cmdProcess.process(clientCmd)
        then:
        message == '$1\r\nb\r\n'

        when:
        message = cmdProcess.process(clientCmd)
        then:
        message == '$3\r\nabc\r\n'

        when:
        message = cmdProcess.process(clientCmd)
        then:
        message == REDIS_EMPTY_STRING

    }

}
