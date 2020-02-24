package org.github.jrbase.process.hash


import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8
import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER
import static org.github.jrbase.dataType.RedisDataType.HASHES

class HSetProcessTest extends Specification {
    CmdProcess cmdProcess = new HSetProcess()
    ClientCmd clientCmd = new ClientCmd()
    //hset key field1 value
    def "Process"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs(args as String[])
        //set key field value

        BackendProxy backendProxy = Mock()
        for (int i = 0; i < clientCmd.getArgLength(); i = i + 2) {
            def buildUpKey = clientCmd.getKey() + "f" + clientCmd.getArgs()[i] + HASHES.getAbbreviation()
            backendProxy.bGetAndPut(buildUpKey, writeUtf8(clientCmd.getArgs()[i + 1])) >> input
        }
        clientCmd.setBackendProxy(backendProxy)

        expect:
        cmdProcess.process(clientCmd) == message
        where:
        input          | args                                     | message

        null           | ["field1", "value1"]                     | ':1\r\n'
        null           | ["field1", "value1", "field2", "value2"] | ':2\r\n'
        'a'.getBytes() | ["field1", "value1"]                     | REDIS_ZORE_INTEGER
        'a'.getBytes() | ["field1", "value1", "field2", "value2"] | REDIS_ZORE_INTEGER
    }


    def "testArgumentsException"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs(["field1", "value1", "error arg"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}
