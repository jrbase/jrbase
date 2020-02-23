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
}
