package io.github.jrbase.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * scan ServerCommand configure
 *
 * @see io.github.jrbase.process.annotation.ScanAnnotationConfigure
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerCommand {
}
