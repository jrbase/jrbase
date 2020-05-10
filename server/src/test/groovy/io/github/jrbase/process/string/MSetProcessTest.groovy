package io.github.jrbase.process.string


import io.github.jrbase.backend.BackendProxy
import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static io.github.jrbase.dataType.RedisDataType.STRINGS

class MSetProcessTest extends Specification {
    CmdProcess cmdProcess = new MSetProcess()
    ClientCmd clientCmd = new ClientCmd()

    def "Process"() {
        given:
        clientCmd.setKey("key")
        String[] arr = args as String[]
        clientCmd.setArgs(arr)
        //set key field value
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation()
        backendProxy.bGetAndPut(buildUpKey, writeUtf8(clientCmd.getArgs()[0])) >> null
        for (int i = 1; i < clientCmd.argLength; i = i + 2) {
            String buildUpKey2 = clientCmd.getArgs()[i] + STRINGS.getAbbreviation()
            backendProxy.bGetAndPut(buildUpKey2, writeUtf8(clientCmd.getArgs()[i + 1])) >> null
        }
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
