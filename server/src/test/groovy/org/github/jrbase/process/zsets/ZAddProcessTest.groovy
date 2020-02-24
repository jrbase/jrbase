package org.github.jrbase.process.zsets


import org.apache.commons.lang.StringUtils
import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8
import static org.github.jrbase.dataType.CommonMessage.REDIS_LIST_DELIMITER
import static org.github.jrbase.dataType.RedisDataType.SORTED_SETS
import static org.github.jrbase.utils.ToolsKeyValue.generateKeyValueMap
import static org.github.jrbase.utils.ToolsString.toRedisListDelimiter

class ZAddProcessTest extends Specification {
    private CmdProcess cmdProcess = new ZAddProcess()
    private ClientCmd clientCmd = new ClientCmd()

    def setup() {
        clientCmd.setKey("a")
    }

    def cleanup() {
    }

    def "processData"() {
        given:
        clientCmd.setArgs(args as String[])
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        String buildUpKey = clientCmd.getKey() + SORTED_SETS.getAbbreviation()
        //
        backendProxy.bGet(buildUpKey) >> originValue
        final Map<String, String> KeyValueMap = generateKeyValueMap(clientCmd.getArgs())
        final String kvResult = readUtf8(originValue)
        if (StringUtils.isEmpty(kvResult)) {
            String result = ZAddProcess.getKvBuildUpResult(KeyValueMap)
            backendProxy.bPut(buildUpKey, result.getBytes())
        } else {
            final String[] arr = kvResult.split(REDIS_LIST_DELIMITER)
            final Map<String, String> bGetKvMap = generateKeyValueMap(arr)
            bGetKvMap.putAll(bGetKvMap)
            String result = ZAddProcess.getKvBuildUpResult(bGetKvMap)
            backendProxy.bPut(buildUpKey, result.getBytes())
        }
        expect:
        cmdProcess.process(clientCmd) == message
        where:
        args                 | originValue                                | message
        ["1", "a"]           | null                                       | ':1\r\n'
        ["1", "b"]           | "".getBytes()                              | ':1\r\n'
        ["1", "b"]           | toRedisListDelimiter("1,b").getBytes()     | ':0\r\n'
        ["2", "b"]           | toRedisListDelimiter("1,b").getBytes()     | ':1\r\n'
        ["1", "b", "2", "c"] | null                                       | ':2\r\n'
        ["1", "b", "2", "c"] | toRedisListDelimiter("1,b").getBytes()     | ':1\r\n'
        ["1", "b", "2", "c"] | toRedisListDelimiter("1,b,2,c").getBytes() | ':0\r\n'
    }

    def "testArgumentsException"() {
        given:
        clientCmd.setArgs([] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
    }
}
