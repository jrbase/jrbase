package org.github.jrbase.process.annotation

import org.github.jrbase.process.CmdProcess
import spock.lang.Specification

class ScanAnnotationConfigureTest extends Specification {

    ScanAnnotationConfigure scanAnnotationConfigure = ScanAnnotationConfigure.newSingleInstance()

    def "ScanAnnotationConfigure"() {
        when:
        CmdProcess cmdProcess = scanAnnotationConfigure.get("hget")
        then:
        cmdProcess.getCmdName() == "hget"
        scanAnnotationConfigure.getCount() == 23
    }

    def "testAnnotationSingleton"() {
        when:
        CmdProcess cmdProcess = scanAnnotationConfigure.get("get")
        CmdProcess cmdProcess2 = scanAnnotationConfigure.get("get")
        then:
        cmdProcess == cmdProcess2
    }

}
