package io.github.jrbase.handler.connect

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.ServerCmdHandler
import spock.lang.Specification

class EchoHandlerTest extends Specification {
    private ServerCmdHandler serverCmdHandler = new EchoHandler()
    private ClientCmd clientCmd = new ClientCmd()

    def "Handle"() {
        given:
        clientCmd.setKey(key)
        expect:
        message == serverCmdHandler.handle(clientCmd)
        where:
        key       | message
        ""        | '-ERR wrong number of arguments for \'echo\' command\r\n'
        "message" | '$7\r\nmessage\r\n'
    }
}
