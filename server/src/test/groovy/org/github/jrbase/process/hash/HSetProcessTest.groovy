package org.github.jrbase.process.hash

import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.handler.CmdHandler
import spock.lang.Ignore
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER

@Ignore
class HSetProcessTest extends Specification {
    //hset key field1 value

    def "Process"() {
        HSetProcess cmdProcess = new HSetProcess()
        ClientCmd clientCmd = new ClientCmd()
        given:
        def chandler = CmdHandler.newSingleInstance(null)
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setKey("key")
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
        HSetProcess cmdProcess = new HSetProcess()
        ClientCmd clientCmd = new ClientCmd()
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs(["field1", "value1", "error arg"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}
