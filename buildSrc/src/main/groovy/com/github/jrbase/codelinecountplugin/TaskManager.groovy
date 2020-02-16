package com.github.jrbase.codelinecountplugin

import com.github.jrbase.codelinecountplugin.counter.CountLine
import com.github.jrbase.codelinecountplugin.counter.GroovyCountLine
import com.github.jrbase.codelinecountplugin.counter.JavaCountLine
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class TaskManager extends DefaultTask {

    @TaskAction
    void startTask() {
        Map<String, Integer> lineCounter = new HashMap<>()

        resolveSourceSets().each { sourceSet ->
            sourceSet.allSource.srcDirs.each { File dir ->
                if (!dir.exists()) return
                dir.eachFileRecurse { File file ->
                    if (file.file) {
                        CountLine countLine
                        if (file.getName().lastIndexOf(".java") > 0) {
                            countLine = new JavaCountLine()
                        } else if (file.getName().lastIndexOf(".groovy") > 0) {
                            countLine = new GroovyCountLine()
                        } else {
                            return
                        }
                        int count = countLine.countLine(file)
                        int value = lineCounter.getOrDefault(countLine.getMyName(), 0)
                        lineCounter.put(countLine.getMyName(), value + count)
                    }
                }
            }
        }

        lineCounter.forEach { key, value ->
            println(key + ":" + value)
        }
    }

    private resolveSourceSets() {
        if (project.plugins.hasPlugin('com.android.library')) {
            project.android.sourceSets
        } else {
            project.sourceSets
        }
    }
}
