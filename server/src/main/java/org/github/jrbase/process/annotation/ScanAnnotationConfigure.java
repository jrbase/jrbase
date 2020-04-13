package org.github.jrbase.process.annotation;

import org.github.jrbase.process.CmdProcess;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScanAnnotationConfigure {


    private final static Map<String, CmdProcess> cmdProcessManager = new HashMap<>();

    public CmdProcess get(String key) {
        return cmdProcessManager.get(key);
    }

    public int getCount() {
        return cmdProcessManager.size();
    }

    public ScanAnnotationConfigure() {
        this.doScan();
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
