package org.github.jrbase.manager


import io.netty.channel.Channel
import org.github.jrbase.dataType.ClientCmd
import org.github.jrbase.process.IgnoreProcess
import org.github.jrbase.process.string.GetProcess
import org.github.jrbase.process.string.SetProcess
import spock.lang.Specification

class CmdManagerTest extends Specification {

    def "ClientCmdToCmdProcess"() {

        expect:
        CmdManager.clientCmdToCmdProcess(new ClientCmd(input)).getClass().getSimpleName() == output

        where:
        input | output
        ''    | IgnoreProcess.getSimpleName()
        'set' | SetProcess.getSimpleName()
        'get' | GetProcess.getSimpleName()
    }


    def "process"() {
        given:
        ClientCmd clientCmd = new ClientCmd("get")
        clientCmd.setKey("key")
        Channel channel = Mock()
        channel.writeAndFlush("\$-1\r\n")
        clientCmd.setChannel(channel)
        clientCmd.setArgs([] as String[])
        when:
        CmdManager.process(clientCmd)
        then:
        0 * CmdManager.sendWrongArgumentMessage(_)
        1 * clientCmd.getChannel().writeAndFlush(_)

    }


}
