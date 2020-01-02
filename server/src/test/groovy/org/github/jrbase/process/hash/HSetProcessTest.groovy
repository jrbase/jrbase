package org.github.jrbase.process.hash

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import org.github.jrbase.process.hash.HSetProcess
import spock.lang.Specification

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8
import static org.github.jrbase.dataType.RedisDataType.HASHES

class HSetProcessTest extends Specification {
    CmdProcess cmdProcess = new HSetProcess()
    ClientCmd clientCmd = new ClientCmd()
    //hset key field1 value
    def "Process"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs(args as String[])
        //set key field value

        RheaKVStore rheaKVStore = Mock()
        for (int i = 0; i < clientCmd.getArgLength(); i = i + 2) {
            def buildUpKey = clientCmd.getKey() + "f" + clientCmd.getArgs()[i] + HASHES.getAbbreviation()
            rheaKVStore.bGetAndPut(buildUpKey, writeUtf8(clientCmd.getArgs()[i + 1])) >> input
        }
        clientCmd.setRheaKVStore(rheaKVStore)

        expect:
        message == cmdProcess.process(clientCmd)
        where:
        input          | args                                     | message

        null           | ["field1", "value1"]                     | ':1\r\n'
        null           | ["field1", "value1", "field2", "value2"] | ':2\r\n'
        'a'.getBytes() | ["field1", "value1"]                     | ':0\r\n'
        'a'.getBytes() | ["field1", "value1", "field2", "value2"] | ':0\r\n'
    }


    def "testArgumentsException"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs(["field1", "value1", "error arg"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}
