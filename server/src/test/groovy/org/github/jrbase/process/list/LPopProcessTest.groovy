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

    def "Process"() {
        given:
        clientCmd.setKey("a")
        RheaKVStore rheaKVStore = Mock()
        clientCmd.setRheaKVStore(rheaKVStore)
        String buildUpKey = clientCmd.getKey() + LISTS.getAbbreviation()
        rheaKVStore.bGet(buildUpKey) >> input
        if (!isEmptyBytes(input)) {
            StringBuilder buildUpValue = new StringBuilder()
            final String resultStr = readUtf8(input)
            final String[] valueArr = resultStr.split(",")
            for (int i = 1; i < valueArr.length - 1; i++) {
                buildUpValue.append(valueArr[i]).append(",")
            }
            if (buildUpValue.length() != 0) {
                buildUpValue.deleteCharAt(buildUpValue.length() - 1)
            }
            //bGetAndPut
            rheaKVStore.bGetAndPut(buildUpKey, writeUtf8(buildUpValue.toString()))
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
