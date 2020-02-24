package org.github.jrbase.process.hash


import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_ZORE_INTEGER
import static org.github.jrbase.dataType.RedisDataType.HASHES

class HLenProcessTest extends Specification {
    def "Process"() {
        CmdProcess cmdProcess = new HLenProcess()
        ClientCmd clientCmd = new ClientCmd()
        given:
        clientCmd.setKey(key)
        clientCmd.setArgs([] as String[])
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        def buildUpKey = clientCmd.getKey() + HASHES.getAbbreviation()
        backendProxy.bGet(buildUpKey) >> result
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        key | result       | message
        "a" | [0, 0, 0, 1] | ':1\r\n'
        "a" | [0, 0, 0, 2] | ':2\r\n'
        "a" | null         | REDIS_ZORE_INTEGER

    }
}
