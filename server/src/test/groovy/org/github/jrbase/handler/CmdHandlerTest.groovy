package org.github.jrbase.handler

import spock.lang.Shared
import spock.lang.Specification

class CmdHandlerTest extends Specification {

    private CmdHandler cmdHandler = new CmdHandler()
    @Shared
    private final static command = '*1\r\n\$7\r\nCOMMAND'
    private final static setCmd = '*3\r\n\$3\r\nset\r\n$1\r\na\r\na\r\nb'

    def "testTranslateClientCmdByCmd"() {
        expect:
        cmdHandler.parseMessage(input).getCmd() == output

        where:
        input   | output
        command | 'command'
        setCmd  | 'set'
    }

    def "testTranslateClientCmdByKey"() {
        expect:
        cmdHandler.parseMessage(input).getKey() == output

        where:
        input   | output
        command | ''
        setCmd  | 'a'
    }

    def "testTranslateClientCmdByKArgs"() {
        expect:
        cmdHandler.parseMessage(input).getArgs().length == output

        where:
        input   | output
        command | 0
        setCmd  | 1
    }


    def "getArgs"() {
        expect:
        cmdHandler.getArgs(redisClientCmdArr as String[]) == (args as String[])
        where:
        redisClientCmdArr                                            | args
        ['*3', '$3', 'set', '$3', 'key']                             | []
        ['*3', '$3', 'set', '$3', 'key', '$5', 'value']              | ['value']
        ['*3', '$3', 'set', '$3', 'key', '$4', 'arg1', '$4', 'arg2'] | ['arg1', 'arg2']
        // *3\r\n$3\r\nset\r\n$3\r\nkey$5\r\nvalue\r\n).split("\r\n")
    }

}
