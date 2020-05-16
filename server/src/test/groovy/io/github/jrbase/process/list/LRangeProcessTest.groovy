package io.github.jrbase.process.list

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_LIST

class LRangeProcessTest extends Specification {

    private CmdProcess cmdProcess = new LRangeProcess()
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
    }

    def "processData"() {
        given:
        clientCmd.setArgs(args as String[])
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args         | message
        ["0", "0"]   | '*1\r\n$3\r\nabc\r\n'
        ["-1", "-1"] | '*1\r\n$1\r\nc\r\n'
        ["100", "0"] | REDIS_EMPTY_LIST
        ["0", "1"]   | '*2\r\n$3\r\nabc\r\n$1\r\nb\r\n'
        ["0", "-1"]  | '*3\r\n$3\r\nabc\r\n$1\r\nb\r\n$1\r\nc\r\n'
        ["1", "-1"]  | '*2\r\n$1\r\nb\r\n$1\r\nc\r\n'

    }

}
