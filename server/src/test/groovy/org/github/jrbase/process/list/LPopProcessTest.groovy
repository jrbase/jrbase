package org.github.jrbase.process.list

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8
import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING
import static org.github.jrbase.dataType.RedisDataType.LISTS
import static org.github.jrbase.utils.Tools.isEmptyBytes

class LPopProcessTest extends Specification {
    private CmdProcess cmdProcess = new LPopProcess()
    private ClientCmd clientCmd = new ClientCmd()

    def setup() {
        clientCmd.setKey("a")
    }

    def "processData"() {
        given:
        RheaKVStore rheaKVStore = Mock()
        clientCmd.setRheaKVStore(rheaKVStore)
        String buildUpKey = clientCmd.getKey() + LISTS.getAbbreviation()
        rheaKVStore.bGet(buildUpKey) >> input
        if (!isEmptyBytes(input)) {
            final String resultStr = readUtf8(input)
            final String[] valueArr = resultStr.split(",")
            String buildUpValue = LPopProcess.getBuildUpValue(valueArr)
            rheaKVStore.bPut(buildUpKey, writeUtf8(buildUpValue))
        }
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        input                | message
        null                 | REDIS_EMPTY_STRING
        "".getBytes()        | REDIS_EMPTY_STRING
        "a".getBytes()       | '$1\r\na\r\n'
        "aa,b".getBytes()    | '$2\r\naa\r\n'
        "abc,b,c".getBytes() | '$3\r\nabc\r\n'
    }

}
