package org.github.jrbase.handler.connect

import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.handler.ServerCmdHandler
import spock.lang.Specification

class PingHandlerTest extends Specification {
    private ServerCmdHandler serverCmdHandler = new PingHandler()
    private ClientCmd clientCmd = new ClientCmd()

    def "Handle"() {
        given:
        clientCmd.setKey(key)
        expect:
        message == serverCmdHandler.handle(clientCmd)
        where:
        key       | message
        ""        | '+PONG\r\n'
        "message" | '$7\r\nmessage\r\n'
    }
}
