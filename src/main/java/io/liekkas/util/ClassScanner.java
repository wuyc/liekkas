package io.liekkas.util;

import io.liekkas.ioc.annotation.Bean;
import io.liekkas.ioc.annotation.Inject;
import io.liekkas.web.annotation.*;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Stream;

import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withAnnotations;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.getAllFields;

public class ClassScanner {

    private Reflections reflections;

    private ClassScanner() {
    }

    public ClassScanner(String packageName) {
        reflections = new Reflections(packageName);
    }

    public Stream<Class<?>> scanBeanClasses() {
        Stream<Class<?>> beans = reflections
                .getTypesAnnotatedWith(Bean.class)
                .stream()
                .filter(clazz -> !clazz.isAnnotation());
        Stream<Class<?>> routes = reflections
                .getTypesAnnotatedWith(Route.class)
                .stream()
                .filter(clazz -> !clazz.isAnnotation());
        return Stream.concat(beans, routes);
    }

    public Stream<Method> scanBeanMethods() {
        return reflections
                .getTypesAnnotatedWith(Bean.class)
                .stream()
                .filter(clazz -> !clazz.isAnnotation())
                .flatMap(clazz -> {
                    Set<Method> methods = getAllMethods(clazz, withAnnotation(Bean.class));
                    return Stream.of(methods.toArray());
                })
                .map(obj -> {
                    Method method = (Method) obj;
                    method.setAccessible(true);
                    return method;
                });
    }

    public Stream<Field> scanInjectFields() {
        return reflections
                .getTypesAnnotatedWith(Bean.class)
                .stream()
                .filter(clazz -> !clazz.isAnnotation())
                .flatMap(clazz -> {
                    Set<Field> fields = getAllFields(clazz, withAnnotation(Inject.class));
                    return Stream.of(fields.toArray());
                })
                .map(obj -> {
                    Field field = (Field) obj;
                    field.setAccessible(true);
                    return field;
                });
    }

    public Stream<Method> scanRoutes() {
        return reflections
                .getTypesAnnotatedWith(Route.class)
                .stream()
                .flatMap(clazz -> {
                    Set<Method> allActions = getAllMethods(clazz, withAnnotations(Route.class));
                    Set<Method> getActions = getAllMethods(clazz, withAnnotations(GetRoute.class));
                    Set<Method> postActions = getAllMethods(clazz, withAnnotations(PostRoute.class));
                    Set<Method> putActions = getAllMethods(clazz, withAnnotations(PutRoute.class));
                    Set<Method> patchActions = getAllMethods(clazz, withAnnotations(PatchRoute.class));
                    Set<Method> deleteActions = getAllMethods(clazz, withAnnotations(DeleteRoute.class));
                    allActions.addAll(getActions);
                    allActions.addAll(postActions);
                    allActions.addAll(putActions);
                    allActions.addAll(patchActions);
                    allActions.addAll(deleteActions);
                    return Stream.of(allActions.toArray());
                })
                .map(obj -> {
                    Method action = (Method) obj;
                    action.setAccessible(true);
                    return action;
                });
    }

}
