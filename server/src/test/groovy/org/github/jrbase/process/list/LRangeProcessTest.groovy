package org.github.jrbase.process.list


import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_LIST
import static org.github.jrbase.dataType.RedisDataType.LISTS
import static org.github.jrbase.utils.ToolsString.toRedisListDelimiter

class LRangeProcessTest extends Specification {

    private CmdProcess cmdProcess = new LRangeProcess()
    private ClientCmd clientCmd = new ClientCmd()

    def setup() {
        clientCmd.setKey("a")
    }

    def "processData"() {
        given:
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        String buildUpKey = clientCmd.getKey() + LISTS.getAbbreviation()
        clientCmd.setArgs(args as String[])
        backendProxy.bGet(buildUpKey) >> input
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args        | input                                   | message
        ["0", "-1"] | "abc,b,c".getBytes()                    | '*3\r\n$3\r\nabc\r\n$1\r\nb\r\n$1\r\nc\r\n'
        ["0", "-1"] | null                                    | REDIS_EMPTY_LIST
        ["0", "-1"] | "".getBytes()                           | REDIS_EMPTY_LIST
        ["0", "-1"] | "a".getBytes()       | '*1\r\n$1\r\na\r\n'
        ["0", "-1"] | toRedisListDelimiter("aa,b").getBytes() | '*2\r\n$2\r\naa\r\n$1\r\nb\r\n'
    }


    def "GetLRangeList"() {
        expect:
        LRangeProcess.getLRangeList(originValueArr as String[], begin as String, end as String) == result
        where:
        originValueArr  | begin  | end    | result
        ["a", "b"]      | "100"  | "100"  | '+(empty list or set)\r\n'
        ["a", "b"]      | "-100" | "-100" | '+(empty list or set)\r\n'
        ["a", "b"]      | "100"  | "-100" | '+(empty list or set)\r\n'
        ["a", "b"]      | "100"  | "-1"   | '+(empty list or set)\r\n'
        ["a", "b"]      | "-1"   | "0"    | '+(empty list or set)\r\n'
        ["a", "b"]      | "0"    | "-1"   | '*2\r\n$1\r\na\r\n$1\r\nb\r\n'
        ["a", "b"]      | "-100" | "100"  | '*2\r\n$1\r\na\r\n$1\r\nb\r\n'
        ["a", "b", "c"] | "0"    | "0"    | '*1\r\n$1\r\na\r\n'
        ["a", "b", "c"] | "1"    | "1"    | '*1\r\n$1\r\nb\r\n'
        ["a", "b", "c"] | "2"    | "2"    | '*1\r\n$1\r\nc\r\n'
        ["a", "b", "c"] | "0"    | "1"    | '*2\r\n$1\r\na\r\n$1\r\nb\r\n'
        ["a", "b", "c"] | "0"    | "2"    | '*3\r\n$1\r\na\r\n$1\r\nb\r\n$1\r\nc\r\n'
        ["a", "b", "c"] | "0"    | "3"    | '*3\r\n$1\r\na\r\n$1\r\nb\r\n$1\r\nc\r\n'
        ["a", "b", "c"] | "-1"   | "4"    | '*1\r\n$1\r\nc\r\n'

    }
}
