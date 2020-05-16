package io.github.jrbase.process.string


import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

class MGetProcessTest extends Specification {
    CmdProcess cmdProcess = new MGetProcess()
    @Shared
    ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        def chandler = CmdHandler.newSingleInstance(null)
        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())

        clientCmd.setKey("a")

        MSetProcess cmdProcess = new MSetProcess()
        clientCmd.setArgs(["a", "b", "b", "c", "c", "d", "d"] as String[])
        cmdProcess.process(clientCmd)
    }

    //get a = b
    def "Process"() {
        given:
        clientCmd.setKey("a")
        clientCmd.setArgs(args as String[])
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args            | message
        []              | '*1\r\n$1\r\na\r\n'
        ["b"]           | '*2\r\n$1\r\na\r\n$1\r\nb\r\n'
        ["nonexisting"] | '*2\r\n$1\r\na\r\n$-1\r\n'
    }

}
