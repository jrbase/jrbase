package io.github.jrbase.process.string


import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

class MSetProcessTest extends Specification {
    CmdProcess cmdProcess = new MSetProcess()
    @Shared
    ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        def chandler = CmdHandler.newSingleInstance(null)
        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setKey("key")
    }

    def "Process"() {
        given:
        clientCmd.setArgs(args as String[])
        //set key field value
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args                         | message
        ["value1", "key2", "value2"] | ':2\r\n'
        ["value1"]                   | ':1\r\n'
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

}
