package org.github.jrbase.process.string

import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8
import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER
import static org.github.jrbase.dataType.RedisDataType.STRINGS

class SetProcessTest extends Specification {
    CmdProcess cmdProcess = new SetProcess()
    ClientCmd clientCmd = new ClientCmd()

    def "Process"() {
        given:
        clientCmd.setKey("key")
        String[] arr = args as String[]
        clientCmd.setArgs(arr)
        //set key field value
        BackendProxy backendProxy = Mock()
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation()
        backendProxy.bGetAndPut(buildUpKey, writeUtf8(clientCmd.getArgs()[0])) >> input
        clientCmd.setBackendProxy(backendProxy)
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        input          | args      | message

        null           | ["value"] | ':1\r\n'
        'a'.getBytes() | ["value"] | REDIS_ZORE_INTEGER
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
