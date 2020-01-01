package org.github.jrbase.process.string

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.execption.ArgumentsException
import org.github.jrbase.process.CmdProcess
import org.github.jrbase.process.string.MSetProcess
import spock.lang.Specification

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8
import static org.github.jrbase.dataType.RedisDataType.STRINGS

class MSetProcessTest extends Specification {
    CmdProcess cmdProcess = new MSetProcess()
    ClientCmd clientCmd = new ClientCmd()

    def "Process"() {
        given:
        clientCmd.setKey("key")
        String[] arr = args as String[]
        clientCmd.setArgs(arr)
        //set key field value
        RheaKVStore rheaKVStore = Mock()
        clientCmd.setRheaKVStore(rheaKVStore)
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation()
        rheaKVStore.bGetAndPut(buildUpKey, writeUtf8(clientCmd.getArgs()[0])) >> null
        for (int i = 1; i < clientCmd.argLength; i = i + 2) {
            String buildUpKey2 = clientCmd.getArgs()[i] + STRINGS.getAbbreviation()
            rheaKVStore.bGetAndPut(buildUpKey2, writeUtf8(clientCmd.getArgs()[i + 1])) >> null
        }
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        args                         | message
        ["value1", "key2", "value2"] | ':2\r\n'
        ["value1"]                   | ':1\r\n'
    }


    def "testArgumentsException"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs(["value", "error arg"] as String[])
        when:
        cmdProcess.checkArguments(clientCmd)
        then:
        thrown ArgumentsException
    }

}
