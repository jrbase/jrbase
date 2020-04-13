package org.github.jrbase.handler.annotation

import org.github.jrbase.handler.ServerCmdHandler
import spock.lang.Specification

class ScanServerAnnotationConfigureTest extends Specification {
    ScanServerAnnotationConfigure scanServerAnnotationConfigure = new ScanServerAnnotationConfigure()

    def "ScanServerAnnotationConfigure"() {
        when:
        ServerCmdHandler serverCmdHandler = scanServerAnnotationConfigure.get("ping")
        then:
        serverCmdHandler.getCmdName() == "ping"
        scanServerAnnotationConfigure.getCount() == 3
    }

    def "testAnnotationSingleton"() {
        when:
        ServerCmdHandler serverCmdHandler = scanServerAnnotationConfigure.get("ping")
        ServerCmdHandler serverCmdHandler2 = scanServerAnnotationConfigure.get("ping")
        then:
        serverCmdHandler == serverCmdHandler2
    }
}
