package io.github.jrbase.handler.connect

import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.ServerCmdHandler
import spock.lang.Specification

class CommandHandlerTest extends Specification {
    private ServerCmdHandler serverCmdHandler = new CommandHandler()
    private ClientCmd clientCmd = new ClientCmd()

    def "Handle"() {
        given:
        clientCmd.setKey(key)
        expect:
        message == serverCmdHandler.handle(clientCmd)
        where:
        key   | message
        ""    | '+OK\r\n'
        "123" | '+OK\r\n'
    }
}
