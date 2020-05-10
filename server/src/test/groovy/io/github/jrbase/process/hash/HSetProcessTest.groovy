package io.github.jrbase.process.hash

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import spock.lang.Shared
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER

class HSetProcessTest extends Specification {
    HSetProcess cmdProcess = new HSetProcess()
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
        clientCmd.setArgs(args as String[])
        //set key field value
        expect:
        cmdProcess.process(clientCmd) == message
        where:
        args                                     | message
        ["field1", "value1"]                     | ':1\r\n'
        ["field1", "value1", "field2", "value2"] | ':1\r\n'
        ["field1", "value1"]                     | REDIS_ZORE_INTEGER
        ["field1", "value1", "field2", "value2"] | REDIS_ZORE_INTEGER
    }

    def "testArgumentsException"() {
        given:
        clientCmd.setArgs(["field1", "value1", "error arg"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}
