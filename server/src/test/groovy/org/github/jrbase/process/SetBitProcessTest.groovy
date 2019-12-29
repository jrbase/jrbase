package org.github.jrbase.process

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.execption.ArgumentsException
import spock.lang.Specification

import static org.github.jrbase.dataType.RedisDataType.STRINGS

class SetBitProcessTest extends Specification {
    CmdProcess cmdProcess = new SetBitProcess()
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
        args       | input          | message
        ['1', '0'] | null           | ':0\r\n'
        ['1', '0'] | "a".getBytes() | ':1\r\n'
        ['2', '0'] | "a".getBytes() | ':1\r\n'
        ['3', '0'] | "a".getBytes() | ':0\r\n'
    }

    def "testArgumentsException"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs([] as String[])
        when:
        cmdProcess.process(clientCmd)
        then:
        thrown ArgumentsException
    }
}
