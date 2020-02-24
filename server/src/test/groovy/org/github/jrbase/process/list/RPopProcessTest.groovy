package org.github.jrbase.process.list

import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import org.github.jrbase.utils.ToolsString
import spock.lang.Specification

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8
import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER
import static org.github.jrbase.dataType.RedisDataType.LISTS
import static org.github.jrbase.utils.Tools.isEmptyBytes
import static org.github.jrbase.utils.ToolsString.toRedisListDelimiter

class RPopProcessTest extends Specification {
    private CmdProcess cmdProcess = new RPopProcess()
    private ClientCmd clientCmd = new ClientCmd()

    def setup() {
        clientCmd.setKey("a")
    }

    def "processData"() {
        given:
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        String buildUpKey = clientCmd.getKey() + LISTS.getAbbreviation()
        backendProxy.bGet(buildUpKey) >> input
        if (!isEmptyBytes(input)) {
            final String resultStr = readUtf8(input)
            final String[] valueArr = resultStr.split(REDIS_LIST_DELIMITER)
            String buildUpValue = ToolsString.getLPopBuildUpValue(valueArr)
            backendProxy.bPut(buildUpKey, writeUtf8(buildUpValue))
        }
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        input                                      | message
        null                                       | REDIS_EMPTY_STRING
        "".getBytes()                              | REDIS_EMPTY_STRING
        "a".getBytes()                             | '$1\r\na\r\n'
        toRedisListDelimiter("aa,b").getBytes()    | '$1\r\nb\r\n'
        toRedisListDelimiter("abc,b,c").getBytes() | '$1\r\nc\r\n'
    }

}
