package com.github.jrbase.codelinecountplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class CodeLineCountPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.tasks.create("countLine", TaskManager)
    }

}
