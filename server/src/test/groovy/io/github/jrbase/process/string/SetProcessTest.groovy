package io.github.jrbase.process.string

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.CmdProcess
import spock.lang.Shared
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.*

class SetProcessTest extends Specification {
    private CmdProcess cmdProcess = new SetProcess()
    @Shared
    def chandler = CmdHandler.newSingleInstance(null)
    @Shared
    private ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())
    }

    def "Process"() {
        given:
        clientCmd.setKey(key)
        String[] arr = args as String[]
        clientCmd.setArgs(arr)
        //set key field value
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        key    | args                          | message
        "key"  | ["value"]                     | REDIS_ONE_INTEGER
        "key"  | ["value"]                     | REDIS_ZORE_INTEGER
        "key2" | ["value2", "nx"]              | REDIS_ONE_INTEGER
        "key2" | ["value2", "ex", "1", "nx"]   | REDIS_EMPTY_STRING
        "key2" | ["value3", "px", "100", "xx"] | REDIS_ZORE_INTEGER
    }

    def "testArgumentsException"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs(["value", "ex", "100", "nx", "error"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }

    def "testArgumentsException2"() {
        given:
        clientCmd.setKey(key)
        String[] arr = args as String[]
        clientCmd.setArgs(arr)
        //set key field value
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        key   | args                              | message
        "key" | ["value", "ex"]                   | REDIS_ERROR_SYNTAX
        "key" | ["value", "error"]                | REDIS_ERROR_SYNTAX
        "key" | ["value", "ex", "not number"]     | REDIS_ERROR_INTEGER_OUT_RANGE
        "key" | ["value", "px", "not number"]     | REDIS_ERROR_INTEGER_OUT_RANGE
        "key" | ["value", "ex", "1", "not xx nx"] | REDIS_ERROR_SYNTAX
    }

}
