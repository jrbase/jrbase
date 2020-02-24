package org.github.jrbase.process.sets


import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER
import static org.github.jrbase.dataType.RedisDataType.SETS
import static org.github.jrbase.utils.ToolsString.toRedisListDelimiter

class SCardProcessTest extends Specification {
    private CmdProcess cmdProcess = new SCardProcess()
    private ClientCmd clientCmd = new ClientCmd()

    def "processData"() {
        given:
        clientCmd.setKey(key)
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation()
        //
        backendProxy.bGet(buildUpKey) >> originValue
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        key   | originValue                            | message
        "abc" | null                                   | REDIS_ZORE_INTEGER
        "abc" | toRedisListDelimiter("a,b").getBytes() | ":2\r\n"
    }
}
