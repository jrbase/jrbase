package org.github.jrbase.process.string

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import org.github.jrbase.process.string.GetBitProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING
import static org.github.jrbase.dataType.RedisDataType.STRINGS

class GetBitProcessTest extends Specification {
    CmdProcess cmdProcess = new GetBitProcess()
    ClientCmd clientCmd = new ClientCmd()

    def "Process"() {
        clientCmd.setKey("a")
        clientCmd.setArgs(args as String[])
        RheaKVStore rheaKVStore = Mock()
        clientCmd.setRheaKVStore(rheaKVStore)
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation()
        rheaKVStore.bGet(buildUpKey) >> input
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args  | input | message
        ['1'] | null  | REDIS_EMPTY_STRING
        ['1'] | "a".getBytes() | ':1\r\n'
        ['2'] | "a".getBytes() | ':1\r\n'
        ['3'] | "a".getBytes() | ':0\r\n'
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
