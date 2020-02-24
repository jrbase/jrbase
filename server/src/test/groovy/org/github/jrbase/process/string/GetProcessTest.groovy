package org.github.jrbase.process.string


import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

import static org.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING
import static org.github.jrbase.dataType.RedisDataType.STRINGS

class GetProcessTest extends Specification {

    //get a = b
    def "Process"() {
        given:
        CmdProcess cmdProcess = new GetProcess()
        ClientCmd clientCmd = new ClientCmd()
        clientCmd.setKey("a") // the is import, "a" must same as clientCmd.getKey() in process: backendProxy.bGet(clientCmd.getKey())
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation()
        backendProxy.bGet(buildUpKey) >> input
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        input          | message
        null           | REDIS_EMPTY_STRING
        "a".getBytes() | '$1\r\na\r\n'
    }

}
