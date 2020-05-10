package io.github.jrbase.process

import io.github.jrbase.dataType.ClientCmd
import spock.lang.Specification

class IgnoreProcessTest extends Specification {

    def "Process"() {
        given:
        CmdProcess cmdProcess = new IgnoreProcess()
        ClientCmd clientCmd = new ClientCmd()
        clientCmd.setCmd(cmd)
        clientCmd.setKey(key)
        clientCmd.setArgs(args as String[])
        expect:
        message == cmdProcess.process(clientCmd)
        where:
        cmd     | key   | args             | message

        null    | "key" | []               | "-ERR unknown command 'null', with args beginning with:key, \r\n"
        "other" | "key" | ["arg1", "arg2"] | "-ERR unknown command 'other', with args beginning with:key, arg1, arg2, \r\n"
    }
}
