package io.github.jrbase.process.sets

import io.github.jrbase.backend.BackendProxy
import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER
import static io.github.jrbase.dataType.RedisDataType.SETS
import static io.github.jrbase.utils.ToolsString.toRedisListDelimiter

class SAddProcessTest extends Specification {

    private CmdProcess cmdProcess = new SAddProcess()
    private ClientCmd clientCmd = new ClientCmd()

    def setup() {
        clientCmd.setKey("a")
    }

    def cleanup() {
    }

    def "processData"() {
        given:
        clientCmd.setArgs(args as String[])
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation()
        //
        backendProxy.bGet(buildUpKey) >> originValue
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args       | originValue                            | message
        ["a", "b"] | "c".getBytes()                         | ':2\r\n'
        ["a"]      | null                                   | ':1\r\n'
        ["a", "b"] | null                                   | ':2\r\n'
        ["a", "a"] | null                                   | ':1\r\n'
        ["a", "b"] | "".getBytes()                          | ':2\r\n'
        ["a", "b"] | "a".getBytes()                         | ':1\r\n'
        ["a", "b"] | toRedisListDelimiter("a,b").getBytes() | REDIS_ZORE_INTEGER
        ["a", "b"] | toRedisListDelimiter("c,d").getBytes() | ':2\r\n'
    }

    def "testArgumentsException"() {
        given:
        clientCmd.setArgs([] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}
