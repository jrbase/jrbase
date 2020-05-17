package io.github.jrbase.handler.annotation

import io.github.jrbase.handler.ServerCmdHandler
import spock.lang.Specification

class ScanServerAnnotationConfigureTest extends Specification {
    ScanServerAnnotationConfigure scanServerAnnotationConfigure = ScanServerAnnotationConfigure.newSingleInstance()

    def "ScanServerAnnotationConfigure"() {
        when:
        ServerCmdHandler serverCmdHandler = scanServerAnnotationConfigure.get("ping")
        then:
        serverCmdHandler.getCmdName() == "ping"
        scanServerAnnotationConfigure.getCount() == 8
    }

    def "testAnnotationSingleton"() {
        when:
        ServerCmdHandler serverCmdHandler = scanServerAnnotationConfigure.get("ping")
        ServerCmdHandler serverCmdHandler2 = scanServerAnnotationConfigure.get("ping")
        then:
        serverCmdHandler == serverCmdHandler2
    }
}
