package com.github.jrbase.codelinecountplugin.counter

import com.github.jrbase.codelinecountplugin.counter.CountLine

class JavaCountLine implements CountLine {

    String getMyName() {
        "java"
    }

    @Override
    int countLine(File file) {
        int count = 0
        def lines = file.readLines()
        lines.forEach {
            line ->
                // skip invalid lines
                // 1. empty
                if (line.trim().isEmpty()) {
                    return 0
                }
                // 2. //
                if (line.trim().startsWith("//")) {
                    return 0
                }
                // 3. /*
                //     *
                //     */
                if (line.startsWith('/*') || line.startsWith('*')) {
                    return 0
                }
                count++
        }
        count
    }
}
