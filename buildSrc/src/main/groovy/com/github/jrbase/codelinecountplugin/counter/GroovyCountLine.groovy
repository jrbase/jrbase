package com.github.jrbase.codelinecountplugin.counter

class GroovyCountLine implements CountLine {

    private JavaCountLine javaCountLine = new JavaCountLine()

    String getMyName() {
        "groovy"
    }

    @Override
    int countLine(File file) {
        return javaCountLine.countLine(file)
    }
}
