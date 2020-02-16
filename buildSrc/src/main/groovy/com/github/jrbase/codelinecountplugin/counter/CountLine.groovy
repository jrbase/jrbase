package com.github.jrbase.codelinecountplugin.counter

interface CountLine {
    /**
     * each file type match one
     * @return e.g.  java,groovy
     */
    String getMyName()
    /**
     * count file content from file
     * @param file counted file from source code
     * @return count line from file
     */
    int countLine(File file);
}