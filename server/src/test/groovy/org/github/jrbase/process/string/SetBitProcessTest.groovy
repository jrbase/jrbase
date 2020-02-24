package org.github.jrbase.process.string


import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER
import static org.github.jrbase.dataType.RedisDataType.STRINGS

class SetBitProcessTest extends Specification {
    CmdProcess cmdProcess = new SetBitProcess()
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
        args       | input          | message
        ['1', '0'] | null           | REDIS_ZORE_INTEGER
        ['1', '0'] | "a".getBytes() | ':1\r\n'
        ['2', '0'] | "a".getBytes() | ':1\r\n'
        ['3', '0'] | "a".getBytes() | REDIS_ZORE_INTEGER
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
