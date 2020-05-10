package io.github.jrbase.process.hash

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.REDIS_ONE_INTEGER
import static io.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER

class HLenProcessTest extends Specification {
    CmdProcess cmdProcess = new HLenProcess()
    @Shared
    def chandler = CmdHandler.newSingleInstance(null)
    @Shared
    private ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        chandler.getDefaultDB().getTable().clear()
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setKey("key")
    }

    def "Process"() {
        given:
        clientCmd.setKey(key)
        clientCmd.setArgs([] as String[])
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        key | message
        "a" | REDIS_ZORE_INTEGER
    }

    def "Process2"() {
        CmdProcess hSetProcess = new HSetProcess()
        given:
        clientCmd.setKey(key)
        clientCmd.setArgs(["f", "v"] as String[])
        expect:
        hSetProcess.process(clientCmd)
        message == cmdProcess.process(clientCmd)
        where:
        key | message
        "a" | REDIS_ONE_INTEGER
    }
}
