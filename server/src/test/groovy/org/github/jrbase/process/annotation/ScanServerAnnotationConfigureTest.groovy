package org.github.jrbase.process.annotation

import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

class ScanServerAnnotationConfigureTest extends Specification {

    def "ScanAnnotationConfigure"() {
        when:
        CmdProcess cmdProcess = ScanAnnotationConfigure.instance().get("hget")
        then:
        cmdProcess.getCmdName() == "hget"
        ScanAnnotationConfigure.instance().getCount() == 20
    }

    def "testAnnotationSingleton"() {
        when:
        CmdProcess cmdProcess = ScanAnnotationConfigure.instance().get("get")
        CmdProcess cmdProcess2 = ScanAnnotationConfigure.instance().get("get")
        then:
        cmdProcess == cmdProcess2
    }

}
