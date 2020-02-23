package org.github.jrbase.handler.annotation;

import org.github.jrbase.handler.ServerCmdHandler;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScanServerAnnotationConfigure {
    private static ScanServerAnnotationConfigure scanServerAnnotationConfigure;

    private final static Map<String, ServerCmdHandler> serverCmdHandlerHashMap = new HashMap<>();

    public ServerCmdHandler get(String key) {
        return serverCmdHandlerHashMap.get(key);
    }

    public int getCount() {
        return serverCmdHandlerHashMap.size();
    }

    public static ScanServerAnnotationConfigure instance() {
        if (scanServerAnnotationConfigure == null) {
            synchronized (ScanServerAnnotationConfigure.class) {
                if (scanServerAnnotationConfigure == null) {
                    scanServerAnnotationConfigure = new ScanServerAnnotationConfigure();
                    scanServerAnnotationConfigure.doScan();
                }
            }
        }
        return scanServerAnnotationConfigure;
    }

    private void doScan() {

        String packageName = "org.github.jrbase.handler";
        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> allClasses =
                reflections.getTypesAnnotatedWith(ServerCommand.class);

        for (Class<?> clazz : allClasses) {
            try {
                final ServerCmdHandler cmdProcess = (ServerCmdHandler) clazz.newInstance();
                serverCmdHandlerHashMap.put(cmdProcess.getCmdName(), cmdProcess);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
