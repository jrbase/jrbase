package com.github.jrbase.codelinecountplugin


import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class CodeLineCountPluginTest {
    @Test
    void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply CodeLineCountPlugin

        assertTrue(project.tasks.countLine instanceof TaskManager)
    }
}
