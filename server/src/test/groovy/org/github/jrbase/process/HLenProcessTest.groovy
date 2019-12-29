package org.github.jrbase.process

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import spock.lang.Specification

import static org.github.jrbase.dataType.RedisDataType.HASHES

class HLenProcessTest extends Specification {
    def "Process"() {
        CmdProcess cmdProcess = new HLenProcess()
        ClientCmd clientCmd = new ClientCmd()
        given:
        clientCmd.setKey(key)
        clientCmd.setArgs([] as String[])
        RheaKVStore rheaKVStore = Mock()
        clientCmd.setRheaKVStore(rheaKVStore)
        def buildUpKey = clientCmd.getKey() + HASHES.getAbbreviation()
        rheaKVStore.bGet(buildUpKey) >> result
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        key | result       | message
        "a" | [0, 0, 0, 1] | ':1\r\n'
        "a" | [0, 0, 0, 2] | ':2\r\n'
        "a" | null         | ':0\r\n'

    }
}
