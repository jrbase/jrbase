package org.github.jrbase.manager

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

}
