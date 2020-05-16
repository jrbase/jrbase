package io.github.jrbase.handler

import spock.lang.Specification

class CommandParseTest extends Specification {
    def "ParseMessageToClientCmd"() {
        expect:
        CommandParse.parseMessageToClientCmd(redisClientCmdArr as String[]).toString() == expect
        where:
        redisClientCmdArr                               | expect
        []                                              | 'ClientCmd{cmd=\'\', key=\'\', args=[]}'
        ['*3', '$3', 'get', '$3', 'key']                | 'ClientCmd{cmd=\'get\', key=\'key\', args=[]}'
        ['*3', '$3', 'get', '$3', 'key', '$5', 'value'] | 'ClientCmd{cmd=\'get\', key=\'key\', args=[value]}'

    }

    def "getArgs"() {
        expect:
        CommandParse.getArgs(redisClientCmdArr as String[]) == (args as String[])
        where:
        redisClientCmdArr                                            | args
        ['*3', '$3', 'get', '$3', 'key']                             | []
        ['*3', '$3', 'set', '$3', 'key', '$5', 'value']              | ['value']
        ['*3', '$3', 'set', '$3', 'key', '$4', 'arg1', '$4', 'arg2'] | ['arg1', 'arg2']
        // *3\r\n$3\r\nset\r\n$3\r\nkey$5\r\nvalue\r\n).split("\r\n")
    }
}
