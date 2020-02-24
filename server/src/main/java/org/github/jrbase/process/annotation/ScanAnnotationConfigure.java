package org.github.jrbase.process.annotation;

import org.github.jrbase.process.CmdProcess;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScanAnnotationConfigure {
    private static ScanAnnotationConfigure scanAnnotationConfigure;

    private final static Map<String, CmdProcess> cmdProcessManager = new HashMap<>();

    public CmdProcess get(String key) {
        return cmdProcessManager.get(key);
    }

    public int getCount() {
        return cmdProcessManager.size();
    }

    public static ScanAnnotationConfigure instance() {
        if (scanAnnotationConfigure == null) {
            synchronized (ScanAnnotationConfigure.class) {
                if (scanAnnotationConfigure == null) {
                    scanAnnotationConfigure = new ScanAnnotationConfigure();
                    scanAnnotationConfigure.doScan();
                }
            }
        }
        return scanAnnotationConfigure;
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
