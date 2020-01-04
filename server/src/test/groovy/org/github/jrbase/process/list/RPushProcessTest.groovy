package org.github.jrbase.process.list

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.RedisDataType.LISTS
import static org.github.jrbase.utils.ToolsString.toRedisListDelimiter

class RPushProcessTest extends Specification {
    private CmdProcess cmdProcess = new RPushProcess()
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
        String buildUpKey = clientCmd.getKey() + LISTS.getAbbreviation()
        //
        rheaKVStore.bGet(buildUpKey) >> input
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args       | input                                  | message
        ["a"]      | null                                   | ':1\r\n'
        ["a", "b"] | null                                   | ':2\r\n'
        ["a", "b"] | "".getBytes()                          | ':2\r\n'
        ["a", "b"] | "a".getBytes()                         | ':3\r\n'
        ["a", "b"] | toRedisListDelimiter("a,b").getBytes() | ':4\r\n'
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
