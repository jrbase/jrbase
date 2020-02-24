package org.github.jrbase.annotation

import org.github.jrbase.process.CmdProcess
import org.github.jrbase.process.annotation.ScanAnnotationConfigure
import spock.lang.Specification

class ScanServerAnnotationConfigureTest extends Specification {

    def "ScanAnnotationConfigure"() {
        when:
        CmdProcess cmdProcess = ScanAnnotationConfigure.instance().get("hget")
        then:
        cmdProcess.getCmdName() == "hget"
        ScanAnnotationConfigure.instance().getCount() == 20
    }

}
