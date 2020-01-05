package org.github.jrbase.process.sets

import com.alipay.sofa.jraft.rhea.client.RheaKVStore
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
        RheaKVStore rheaKVStore = Mock()
        clientCmd.setRheaKVStore(rheaKVStore)
        String buildUpKey = clientCmd.getKey() + SETS.getAbbreviation()
        //
        rheaKVStore.bGet(buildUpKey) >> originValue
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        key   | originValue                            | message
        "abc" | null                                   | REDIS_ZORE_INTEGER
        "abc" | toRedisListDelimiter("a,b").getBytes() | ":2\r\n"
    }
}
