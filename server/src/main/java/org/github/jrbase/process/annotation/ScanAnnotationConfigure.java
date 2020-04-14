package org.github.jrbase.process.annotation;

import org.github.jrbase.process.CmdProcess;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScanAnnotationConfigure {

    private final static Map<String, CmdProcess> cmdProcessManager = new HashMap<>();
    private static ScanAnnotationConfigure singleInstance;

    public static ScanAnnotationConfigure newSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new ScanAnnotationConfigure();
            singleInstance.doScan();
        }
        return singleInstance;
    }

    public CmdProcess get(String key) {
        return cmdProcessManager.get(key);
    }

    public int getCount() {
        return cmdProcessManager.size();
    }

    private ScanAnnotationConfigure() {
    }

    private void doScan() {
        String packageName = "org.github.jrbase.process";
        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> allClasses =
                reflections.getTypesAnnotatedWith(KeyCommand.class);

        for (Class<?> clazz : allClasses) {
            try {
                final CmdProcess cmdProcess = (CmdProcess) clazz.newInstance();
                cmdProcessManager.put(cmdProcess.getCmdName(), cmdProcess);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
