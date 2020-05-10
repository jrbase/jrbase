package io.github.jrbase.process.string

import io.github.jrbase.backend.BackendProxy
import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING
import static io.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER
import static io.github.jrbase.dataType.RedisDataType.STRINGS

class GetBitProcessTest extends Specification {
    CmdProcess cmdProcess = new GetBitProcess()
    ClientCmd clientCmd = new ClientCmd()

    def "Process"() {
        clientCmd.setKey("a")
        clientCmd.setArgs(args as String[])
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation()
        backendProxy.bGet(buildUpKey) >> input
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args  | input          | message
        ['1'] | null           | REDIS_EMPTY_STRING
        ['1'] | "a".getBytes() | ':1\r\n'
        ['2'] | "a".getBytes() | ':1\r\n'
        ['3'] | "a".getBytes() | REDIS_ZORE_INTEGER
    }


    def "testArgumentsException"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs([] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}
