package org.github.jrbase.process.sets


import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.RedisDataType.SETS
import static org.github.jrbase.utils.ToolsString.toRedisListDelimiter

class SPopProcessTest extends Specification {
    private CmdProcess cmdProcess = new SPopProcess()
    private ClientCmd clientCmd = new ClientCmd()

    def setup() {
        clientCmd.setKey("a")
    }

    def cleanup() {
    }

    def "processErrorData"() {
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
        args       | originValue                              | message
        ["1", "2"] | "c".getBytes()                           | '-ERR syntax error\r\n'
        []         | "c".getBytes()                           | '*1\r\n$1\r\nc\r\n'
        ["a"]      | toRedisListDelimiter("a,b,c").getBytes() | '-ERR value is not an integer or out of range\r\n'
    }

    def "processReturnAllData"() {
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
        args   | originValue                              | message
        ["3"]  | toRedisListDelimiter("a,b,c").getBytes() | '*3\r\n$1\r\na\r\n$1\r\nb\r\n$1\r\nc\r\n'
        ["10"] | toRedisListDelimiter("a,b,c").getBytes() | '*3\r\n$1\r\na\r\n$1\r\nb\r\n$1\r\nc\r\n'
    }

    def "processArgsCount"() {
        given:
        clientCmd.setArgs(args as String[])
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation()
        //
        backendProxy.bGet(buildUpKey) >> originValue
        expect:
        count == cmdProcess.process(clientCmd).split("\r\n")[0]
        where:
        args  | originValue                                  | count
        ["2"] | toRedisListDelimiter("a,b,c").getBytes()     | "*2"
        ["4"] | toRedisListDelimiter("a,b,c,d,f").getBytes() | "*4"
    }
    //process data


    def "testArgumentsException"() {
        given:
        clientCmd.setArgs([] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        result
    }

    def "GetPopResult"() {
        when:
        def result = cmdProcess.getPopResult(["a", "b", 'd'])
        then:
        result == '*3\r\n$1\r\na\r\n$1\r\nb\r\n$1\r\nd\r\n'
    }

    def "testMakeRandomSets"() {
        when:
        def result = cmdProcess.makeRandomSets(10, 8)
        then:
        result.size() == 8
    }

}
