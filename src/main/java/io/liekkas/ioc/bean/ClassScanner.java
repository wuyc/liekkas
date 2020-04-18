package io.liekkas.ioc.bean;

import io.liekkas.ioc.annotation.Bean;
import io.liekkas.ioc.annotation.Inject;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Stream;

public class ClassScanner {

    private Reflections reflections;

    private ClassScanner() {
    }

    public ClassScanner(String packageName) {
        reflections = new Reflections(packageName);
    }

    public Stream<Class<?>> findBeanClasses() {
        return reflections
                .getTypesAnnotatedWith(Bean.class)
                .stream()
                .filter(clazz -> !clazz.isAnnotation());
    }

    public Stream<Method> findBeanMethods() {
        return reflections
                .getTypesAnnotatedWith(Bean.class)
                .stream()
                .filter(clazz -> !clazz.isAnnotation())
                .flatMap(clazz -> {
                    Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(Bean.class));
                    return Stream.of(methods.toArray());
                })
                .map(obj -> {
                    Method method = (Method) obj;
                    method.setAccessible(true);
                    return method;
                });
    }

    public Stream<Field> findInjectFields() {
        return reflections
                .getTypesAnnotatedWith(Bean.class)
                .stream()
                .filter(clazz -> !clazz.isAnnotation())
                .flatMap(clazz -> {
                    Set<Field> fields = ReflectionUtils.getAllFields(clazz, ReflectionUtils.withAnnotation(Inject.class));
                    return Stream.of(fields.toArray());
                })
                .map(obj -> {
                    Field field = (Field) obj;
                    field.setAccessible(true);
                    return field;
                });
    }

}
