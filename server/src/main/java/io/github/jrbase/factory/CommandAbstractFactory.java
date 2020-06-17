package io.github.jrbase.factory;

import io.github.jrbase.handler.ServerCmdHandler;
import io.github.jrbase.handler.annotation.ServerCommand;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;
import io.github.jrbase.process.annotation.ScanAnnotationConfigure;

public class CommandAbstractFactory {

    public static ScanAnnotationConfigure<ServerCmdHandler> newHandlerAnnotation() {
        ScanAnnotationConfigure<ServerCmdHandler> scanAnnotationConfigure = new ScanAnnotationConfigure<>();
        scanAnnotationConfigure.doScan("io.github.jrbase.handler", ServerCommand.class,
                (item) -> scanAnnotationConfigure.put(item.getCmdName(), item)
        );
        return scanAnnotationConfigure;
    }

    public static ScanAnnotationConfigure<CmdProcess> newProcessAnnotationConfigure() {
        ScanAnnotationConfigure<CmdProcess> scanAnnotationConfigure = new ScanAnnotationConfigure<>();
        scanAnnotationConfigure.doScan("io.github.jrbase.process", KeyCommand.class,
                (item) -> scanAnnotationConfigure.put(item.getCmdName(), item)
        );
        return scanAnnotationConfigure;
    }
}
