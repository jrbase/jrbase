package io.github.jrbase.process.annotation;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * scan two @interface
 *
 * @param <T>
 * @see io.github.jrbase.process.annotation.KeyCommand
 * @see io.github.jrbase.handler.annotation.ServerCommand
 * @see io.github.jrbase.process.CmdProcess
 * @see io.github.jrbase.handler.ServerCmdHandler
 */
public class ScanAnnotationConfigure<T> {

    private static final Logger logger = Logger.getLogger(ScanAnnotationConfigure.class);

    private final Map<String, T> cmdProcessManager = new HashMap<>();

    public void doScan(String packageName, final Class<? extends Annotation> annotation, Consumer<T> consumer) {
        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> allClasses =
                reflections.getTypesAnnotatedWith(annotation);
        for (Class<?> clazz : allClasses) {
            logger.info(clazz.getName());

            try {
                @SuppressWarnings("unchecked") final T cmdProcess = (T) clazz.newInstance();
                consumer.accept(cmdProcess);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e);
                throw new RuntimeException(e);
            }
        }
    }

    public T get(String cmd) {
        return cmdProcessManager.get(cmd);
    }

    public int size() {
        return cmdProcessManager.size();
    }

    public void put(String cmdName, T item) {
        cmdProcessManager.put(cmdName, item);
    }
}
