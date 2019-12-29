package org.github.jrbase.process

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import spock.lang.Specification

import static org.github.jrbase.dataType.RedisDataType.STRINGS

class GetProcessTest extends Specification {
    //TODO: test GetProcess --learn groovy test

    //get a = b
    def "Process"() {
        given:
        CmdProcess cmdProcess = new GetProcess()
        ClientCmd clientCmd = new ClientCmd()
        clientCmd.setKey("a") // the is import, "a" must same as  clientCmd.getKey() in process: rheaKVStore.bGet(clientCmd.getKey())
        RheaKVStore rheaKVStore = Mock()
        clientCmd.setRheaKVStore(rheaKVStore)
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation()
        rheaKVStore.bGet(buildUpKey) >> input
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        input          | message
        null           | '$-1\r\n'
        "a".getBytes() | '$1\r\na\r\n'
    }

}
