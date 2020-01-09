package org.github.jrbase.process.hash

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import org.github.jrbase.process.hash.HGetProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING
import static org.github.jrbase.dataType.RedisDataType.HASHES

class HGetProcessTest extends Specification {
    private CmdProcess cmdProcess = new HGetProcess()
    private ClientCmd clientCmd = new ClientCmd()

    //hget key field1
    def "Process"() {
        given:
        clientCmd.setKey("a")
        clientCmd.setArgs(["field1"] as String[])
        RheaKVStore rheaKVStore = Mock()
        def buildUpKey = clientCmd.getKey() + "f" + clientCmd.getArgs()[0] + HASHES.getAbbreviation()
        rheaKVStore.bGet(buildUpKey) >> input
        clientCmd.setRheaKVStore(rheaKVStore)
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        input               | message
        null | REDIS_EMPTY_STRING
        "value1".getBytes() | '$6\r\nvalue1\r\n'
    }

    def "testArgumentsException"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs(["value", "error arg"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }

    def "testArgumentsException22"() {
        given:
        clientCmd.setKey("key")
        clientCmd.setArgs([] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}