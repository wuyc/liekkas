package io.liekkas.ioc;

import io.liekkas.ioc.annotation.Bean;
import io.liekkas.ioc.annotation.Inject;
import io.liekkas.ioc.entity.ClassEntity;
import io.liekkas.ioc.reader.ClassScanner;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
public class BeanManager {

    public static void initBean(String packageName) {
        LiekkasIoc ioc = LiekkasIoc.getInstance();
        Supplier<Stream<ClassEntity>> classes = () -> ClassScanner.getClasses(packageName).stream();
        // inject class bean.
        classes
                .get()
                .filter(classEntity -> {
                    Class<?> clazz = classEntity.getClazz();
                    Bean bean = clazz.getDeclaredAnnotation(Bean.class);
                    return null != bean;
                })
                .map(classEntity -> classEntity.getClazz())
                .forEach(clazz -> ioc.registerBean(clazz));
        // inject method return bean.
        classes
                .get()
                .flatMap(classEntity -> {
                    Class<?> clazz = classEntity.getClazz();
                    Method[] methods = clazz.getDeclaredMethods();
                    return Stream.of(methods);
                })
                .filter(method -> {
                    Bean clazzAnnotation = method.getDeclaringClass().getDeclaredAnnotation(Bean.class);
                    Bean methodAnnotation = method.getDeclaredAnnotation(Bean.class);
                    return null != clazzAnnotation && null != methodAnnotation;
                })
                .forEach(method -> {
                    method.setAccessible(true);
                    Class<?> clazz = method.getDeclaringClass();
                    Class<?>[] methodParams = method.getParameterTypes();
                    List<Object> paramInstances = new ArrayList<>();
                    for (Class<?> param : methodParams) {
                        Object bean = ioc.getBean(param);
                        if (null == bean) {
                            log.error("Inject bean [{}] error.", method.getReturnType().getName());
                            return;
                        }
                        paramInstances.add(bean);
                    }
                    try {
                        Object ret = method.invoke(ioc.getBean(clazz), paramInstances.toArray());
                        ioc.registerBean(ret);
                    } catch (ReflectiveOperationException e) {
                        log.error("Inject bean error.", e);
                    }
                });
        // inject bean from pool.
        classes
                .get()
                .flatMap(classEntity -> {
                    Class<?> clazz = classEntity.getClazz();
                    Field[] fields = clazz.getDeclaredFields();
                    return Stream.of(fields);
                })
                .filter(field -> {
                    Class<?> clazz = field.getDeclaringClass();
                    Bean bean = clazz.getAnnotation(Bean.class);
                    Inject inject = field.getDeclaredAnnotation(Inject.class);
                    return null != bean && null != inject;
                })
                .forEach(field -> {
                    field.setAccessible(true);
                    Class<?> clazz = field.getDeclaringClass();
                    Class<?> type = field.getType();
                    try {
                        field.set(ioc.getBean(clazz), ioc.getBean(type));
                    } catch (IllegalAccessException e) {
                        log.error("Inject bean error.", e);
                    }
                });
    }

}
