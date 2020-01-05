package org.github.jrbase.process.sets

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER
import static org.github.jrbase.dataType.RedisDataType.SETS
import static org.github.jrbase.utils.ToolsString.toRedisListDelimiter

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
        RheaKVStore rheaKVStore = Mock()
        clientCmd.setRheaKVStore(rheaKVStore)
        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation()
        //
        rheaKVStore.bGet(buildUpKey) >> originValue
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
