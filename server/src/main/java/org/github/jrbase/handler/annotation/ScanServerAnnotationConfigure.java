package org.github.jrbase.handler.annotation;

import org.github.jrbase.handler.ServerCmdHandler;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScanServerAnnotationConfigure {

    private static ScanServerAnnotationConfigure singleInstance;

    private static final Map<String, ServerCmdHandler> serverCmdHandlerHashMap = new HashMap<>();

    public static ScanServerAnnotationConfigure newSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new ScanServerAnnotationConfigure();
            singleInstance.doScan();
        }
        return singleInstance;
    }

    public ServerCmdHandler get(String key) {
        return serverCmdHandlerHashMap.get(key);
    }

    public int getCount() {
        return serverCmdHandlerHashMap.size();
    }

    private ScanServerAnnotationConfigure() {
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
