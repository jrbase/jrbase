package org.github.jrbase.process.hash


import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.handler.CmdHandler
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_ONE_INTEGER
import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER

class HLenProcessTest extends Specification {
    def "Process"() {
        CmdProcess cmdProcess = new HLenProcess()
        ClientCmd clientCmd = new ClientCmd()
        def chandler = CmdHandler.newSingleInstance(null)
        clientCmd.setDb(chandler.getDefaultDB())
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
        CmdProcess cmdProcess = new HLenProcess()
        ClientCmd clientCmd = new ClientCmd()
        def chandler = CmdHandler.newSingleInstance(null)
        clientCmd.setDb(chandler.getDefaultDB())
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
