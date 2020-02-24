package org.github.jrbase.manager


import io.netty.channel.Channel
import org.github.jrbase.backend.BackendProxy
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.dataType.Cmd
import org.github.jrbase.process.string.GetProcess
import org.github.jrbase.process.string.SetProcess
import org.junit.Assert
import spock.lang.Specification

import static org.github.jrbase.dataType.RedisDataType.STRINGS

class CmdManagerTest extends Specification {

    def "testCmdProcessManagerForgetRegisterCmdProcess2"() {
        given:
        ClientCmd clientCmd = new ClientCmd()
        def keys = Cmd.getLookup().values()
        keys.forEach({ cmd ->
            clientCmd.setCmd(cmd.cmdName)
            def cmdProcess = CmdManager.clientCmdToCmdProcess(clientCmd)
            Assert.assertNotNull("you forget register command: " + clientCmd.getCmd(), cmdProcess)
        })
    }

    def "ClientCmdToCmdProcess0"() {
        expect:
        CmdManager.clientCmdToCmdProcess(new ClientCmd(input)) == output

        where:
        input     | output
        ''        | null
        'unknown' | null
    }

    def "ClientCmdToCmdProcess"() {
        expect:
        CmdManager.clientCmdToCmdProcess(new ClientCmd(input)).getClass().getSimpleName() == output

        where:
        input | output
        'set' | SetProcess.getSimpleName()
        'get' | GetProcess.getSimpleName()
    }

    def "processSuccess"() {
        given:
        ClientCmd clientCmd = new ClientCmd("get")
        clientCmd.setKey("key")
        final BackendProxy backendProxy = Mock()
        clientCmd.setBackendProxy(backendProxy)
        String buildUpKey = clientCmd.getKey() + STRINGS.getAbbreviation()
        backendProxy.bGet(buildUpKey) >> null

        Channel channel = Mock()
        channel.writeAndFlush("\$-1\r\n")
        clientCmd.setChannel(channel)
        clientCmd.setArgs([] as String[])
        when:
        CmdManager.process(clientCmd)
        then:
        1 * clientCmd.getChannel().writeAndFlush(_)
    }

    def "processErrorCmd"() {
        given:
        ClientCmd clientCmd = new ClientCmd("")
        clientCmd.setKey("")
        clientCmd.setArgs([] as String[])
        Channel channel = Mock()
        channel.writeAndFlush("\$-1\r\n")
        clientCmd.setChannel(channel)

        when:
        CmdManager.process(clientCmd)
        then:
        1 * channel.writeAndFlush('-ERR wrong number of arguments for \'\' command\r\n')
    }

    def "processErrorCmd123"() {
        given:
        ClientCmd clientCmd = new ClientCmd("123")
        clientCmd.setKey("")
        clientCmd.setArgs([] as String[])
        Channel channel = Mock()
        channel.writeAndFlush("\$-1\r\n")
        clientCmd.setChannel(channel)

        when:
        CmdManager.process(clientCmd)
        then:
        1 * channel.writeAndFlush('-ERR wrong number of arguments for \'123\' command\r\n')
    }

    def "processErrorKey"() {
        given:
        ClientCmd clientCmd = new ClientCmd("get")
        clientCmd.setKey("")
        clientCmd.setArgs([] as String[])
        Channel channel = Mock()
        channel.writeAndFlush("\$-1\r\n")
        clientCmd.setChannel(channel)

        when:
        CmdManager.process(clientCmd)
        then:
        1 * channel.writeAndFlush('-ERR wrong number of arguments for \'get\' command\r\n')
    }

    def "processErrorArgs"() {
        given:
        ClientCmd clientCmd = new ClientCmd("set")
        clientCmd.setKey("a")
        clientCmd.setArgs([] as String[])
        Channel channel = Mock()
        channel.writeAndFlush("\$-1\r\n")
        clientCmd.setChannel(channel)

        when:
        CmdManager.process(clientCmd)
        then:
        1 * channel.writeAndFlush('-ERR wrong number of arguments for \'set\' command\r\n')
    }


}
