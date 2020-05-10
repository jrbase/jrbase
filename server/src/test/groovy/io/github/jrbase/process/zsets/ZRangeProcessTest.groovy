package io.github.jrbase.process.zsets


import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.database.ZSortRedisValue
import io.github.jrbase.handler.CmdHandler
import spock.lang.Shared
import spock.lang.Specification

import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_INTEGER_OUT_RANGE
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_SYNTAX

class ZRangeProcessTest extends Specification {
    private io.github.jrbase.process.CmdProcess cmdProcess = new ZRangeProcess()
    private io.github.jrbase.process.CmdProcess zAddProcess = new ZAddProcess()
    @Shared
    def chandler = CmdHandler.newSingleInstance(null)
    @Shared
    private ClientCmd clientCmd = new ClientCmd()

    def setupSpec() {
        chandler.getDefaultDB().getTable().clear()
        clientCmd.setKey("key")
        ZSortRedisValue zSortRedisValue = new ZSortRedisValue()
        zSortRedisValue.put("m1", 1)
        zSortRedisValue.put("m222", 2)
        zSortRedisValue.put("m369", 3)
        chandler.getDefaultDB().getTable().put("key", zSortRedisValue)
    }


    def "Process"() {
        given:
        clientCmd.setDb(chandler.getDefaultDB())
        clientCmd.setArgs(args as String[])
        cmdProcess.isCorrectArguments(clientCmd)

        expect:
        cmdProcess.process(clientCmd) == message
        where:
        args                     | message
        ["0", "0"]               | '*1\r\n$2\r\nm1\r\n'
        ["0", "0", "withscores"] | '*1\r\n$2\r\nm1\r\n$1\r\n1\r\n'
        ["0", "-1"]              | '*3\r\n$2\r\nm1\r\n$4\r\nm222\r\n$4\r\nm369\r\n'
    }

    def "IsCorrectArgumentsSize"() {
        given:
        clientCmd.setArgs(args as String[])
        expect:
        result == cmdProcess.isCorrectArguments(clientCmd)
        where:
        args                            | result
        []                              | false
        ["1"]                           | false
        ["1", "2"]                      | true
        ["1", "2", "withscores"]        | true
        ["1", "2", "withscores", "123"] | false

    }

    def "IsCorrectArgumentsByNotNumberArgs"() {
        given:
        clientCmd.setArgs(["a", "b"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
        clientCmd.getError() == REDIS_ERROR_INTEGER_OUT_RANGE
    }

    def "IsCorrectArgumentsByWithScores"() {
        given:
        clientCmd.setArgs(["1", "1", "error withScores"] as String[])
        when:
        def result = cmdProcess.isCorrectArguments(clientCmd)
        then:
        !result
        clientCmd.getError() == REDIS_ERROR_SYNTAX
    }
}
