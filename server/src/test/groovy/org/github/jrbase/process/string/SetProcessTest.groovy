package org.github.jrbase.process.string

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.execption.ArgumentsException
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8
import static org.github.jrbase.dataType.RedisDataType.STRINGS

class SetProcessTest extends Specification {
    CmdProcess cmdProcess = new SetProcess()
    ClientCmd clientCmd = new ClientCmd()

    def "Process"() {
        given:
        clientCmd.setKey("key")
        String[] arr = args as String[]
        clientCmd.setArgs(arr)
        //set key field value
        RheaKVStore rheaKVStore = Mock()
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation()
        rheaKVStore.bGetAndPut(buildUpKey, writeUtf8(clientCmd.getArgs()[0])) >> input
        clientCmd.setRheaKVStore(rheaKVStore)
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        input          | args      | message

        null           | ["value"] | ':1\r\n'
        'a'.getBytes() | ["value"] | ':0\r\n'
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
