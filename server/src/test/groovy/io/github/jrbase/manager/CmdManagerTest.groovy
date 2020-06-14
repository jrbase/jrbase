package io.github.jrbase.manager

import io.github.jrbase.common.datatype.Cmd
import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.handler.CmdHandler
import io.github.jrbase.process.string.GetProcess
import io.github.jrbase.process.string.SetProcess
import io.netty.channel.Channel
import org.junit.Assert
import spock.lang.Specification

class CmdManagerTest extends Specification {

    CmdManager cmdManager = CmdManager.newSingleInstance()

    def "testCmdProcessManagerForgetRegisterCmdProcess2"() {
        given:
        ClientCmd clientCmd = new ClientCmd()
        def keys = Cmd.getLookup().values()
        keys.forEach({ cmd ->
            clientCmd.setCmd(cmd.cmdName)
            def cmdProcess = cmdManager.clientCmdToCmdProcess(clientCmd)
            Assert.assertNotNull("you forget register command: " + clientCmd.getCmd(), cmdProcess)
        })
    }

    def "ClientCmdToCmdProcess0"() {
        expect:
        cmdManager.clientCmdToCmdProcess(new ClientCmd(input)) == output

        where:
        input     | output
        ''        | null
        'unknown' | null
    }

    def "ClientCmdToCmdProcess"() {
        expect:
        cmdManager.clientCmdToCmdProcess(new ClientCmd(input)).getClass().getSimpleName() == output

        where:
        input | output
        'set' | SetProcess.getSimpleName()
        'get' | GetProcess.getSimpleName()
    }

    //TODO: async test
    def "processSuccess"() {
        def chandler = CmdHandler.newSingleInstance(null)

        given:
        ClientCmd clientCmd = new ClientCmd("get")
        clientCmd.setKey("key")

        chandler.getDefaultDB().clear()
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setKey("key")

        Channel channel = Mock()
        channel.writeAndFlush("\$-1\r\n")
        clientCmd.setChannel(channel)
        clientCmd.setArgs([] as String[])
        when:
        cmdManager.process(clientCmd)
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
        cmdManager.process(clientCmd)
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
        cmdManager.process(clientCmd)
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
        cmdManager.process(clientCmd)
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
        cmdManager.process(clientCmd)
        then:
        1 * channel.writeAndFlush('-ERR wrong number of arguments for \'set\' command\r\n')
    }


}
