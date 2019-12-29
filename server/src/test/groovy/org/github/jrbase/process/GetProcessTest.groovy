package org.github.jrbase.process

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.dataType.RedisDataType
import spock.lang.Specification

class GetProcessTest extends Specification {
    //TODO: test GetProcess --learn groovy test
    //get a = b
    def "Process"() {
        given:
        GetProcess cmdProcess = new GetProcess()
        ClientCmd clientCmd = new ClientCmd()
        clientCmd.setCmd(GetProcess.getName())
        clientCmd.setKey("a") // the is import, "a" must same as  clientCmd.getKey() in process: rheaKVStore.bGet(clientCmd.getKey())
        RheaKVStore rheaKVStore = Mock()
        def keyWithAbbreviation = clientCmd.getKeyAddAbbreviation(RedisDataType.STRINGS.getAbbreviation())
        rheaKVStore.bGet(keyWithAbbreviation) >> input
        clientCmd.setRheaKVStore(rheaKVStore)
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        input | message
        null  | '$-1\r\n'
        [97]  | '$1\r\na\r\n'
    }

}
