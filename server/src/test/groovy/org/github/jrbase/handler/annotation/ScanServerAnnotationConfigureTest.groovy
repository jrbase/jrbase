package org.github.jrbase.handler.annotation

import org.github.jrbase.handler.ServerCmdHandler
import spock.lang.Specification

class ScanServerAnnotationConfigureTest extends Specification {
    def "ScanServerAnnotationConfigure"() {
        when:
        ServerCmdHandler serverCmdHandler = ScanServerAnnotationConfigure.instance().get("ping")
        then:
        serverCmdHandler.getCmdName() == "ping"
        ScanServerAnnotationConfigure.instance().getCount() == 3
    }

    def "testAnnotationSingleton"() {
        when:
        ServerCmdHandler serverCmdHandler = ScanServerAnnotationConfigure.instance().get("ping")
        ServerCmdHandler serverCmdHandler2 = ScanServerAnnotationConfigure.instance().get("ping")
        then:
        serverCmdHandler == serverCmdHandler2
    }
}
