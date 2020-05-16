package io.github.jrbase.process.annotation


import spock.lang.Specification

class ScanAnnotationConfigureTest extends Specification {

    ScanAnnotationConfigure scanAnnotationConfigure = ScanAnnotationConfigure.newSingleInstance()

    def "ScanAnnotationConfigure"() {
        when:
        io.github.jrbase.process.CmdProcess cmdProcess = scanAnnotationConfigure.get("hget")
        then:
        cmdProcess.getCmdName() == "hget"
        scanAnnotationConfigure.getCount() == 23
    }

    def "testAnnotationSingleton"() {
        when:
        io.github.jrbase.process.CmdProcess cmdProcess = scanAnnotationConfigure.get("get")
        io.github.jrbase.process.CmdProcess cmdProcess2 = scanAnnotationConfigure.get("get")
        then:
        cmdProcess == cmdProcess2
    }

}
