package io.liekkas.ioc.bean;

import io.liekkas.exception.LiekkasException;
import io.liekkas.ioc.Ioc;
import io.liekkas.ioc.LiekkasIoc;
import io.liekkas.util.ClassScanner;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;

@Slf4j
public class BeanManager {

    private BeanManager() {}

    public static void init(String packageName) {
        Ioc ioc = LiekkasIoc.getInstance();
        ClassScanner scanner = new ClassScanner(packageName);

        // register bean from the classes with @Bean annotation.
        scanner.scanBeanClasses()
                .forEach(clazz -> ioc.registerBean(clazz));

        // register bean from the classes method with @Bean annotation.
        scanner.scanBeanMethods()
                .forEach(method -> {
                    Class<?> clazz = method.getDeclaringClass();
                    Parameter[] methodParams = method.getParameters();
                    BeanMethodArgumentResolver resolver = new BeanMethodArgumentResolver();
                    Object[] args = resolver.resolveArgument(methodParams);
                    try {
                        Object ret = method.invoke(ioc.getBean(clazz), args);
                        ioc.registerBean(ret);
                    } catch (ReflectiveOperationException e) {
                        throw new LiekkasException("Register bean failed.", e);
                    }
                });

        // inject bean from pool.
        scanner.scanInjectFields()
                .forEach(field -> {
                    Class<?> clazz = field.getDeclaringClass();
                    Class<?> type = field.getType();
                    try {
                        field.set(ioc.getBean(clazz), ioc.getBean(type));
                    } catch (IllegalAccessException e) {
                        throw new LiekkasException("Inject bean failed.", e);
                    }
                });
    }

}
