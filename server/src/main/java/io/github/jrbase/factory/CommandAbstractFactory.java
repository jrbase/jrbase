package io.github.jrbase.factory;

import io.github.jrbase.handler.annotation.ScanServerAnnotationConfigure;
import io.github.jrbase.process.annotation.ScanAnnotationConfigure;

public class CommandAbstractFactory {
    public static ScanServerAnnotationConfigure newHandlerAnnotation() {
        return ScanServerAnnotationConfigure.newSingleInstance();
    }

    public static ScanAnnotationConfigure newProcessAnnotationConfigure() {
        return ScanAnnotationConfigure.newSingleInstance();
    }
}
