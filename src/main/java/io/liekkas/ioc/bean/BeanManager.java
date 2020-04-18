package io.liekkas.ioc.bean;

import io.liekkas.exception.LiekkasException;
import io.liekkas.ioc.LiekkasIoc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanManager {

    private BeanManager() {}

    public static void init(String packageName) {
        LiekkasIoc ioc = LiekkasIoc.getInstance();
        ClassScanner scanner = new ClassScanner(packageName);

        // register bean from the classes with @Bean annotation.
        scanner.findBeanClasses()
                .forEach(clazz -> ioc.registerBean(clazz));

        // register bean from the classes method with @Bean annotation.
        scanner.findBeanMethods()
                .forEach(method -> {
                    Class<?> clazz = method.getDeclaringClass();
                    Class<?>[] methodArgs = method.getParameterTypes();
                    BeanMethodArgumentResolver resolver = new BeanMethodArgumentResolver();
                    Object[] args = resolver.resolveArgument(methodArgs);
                    try {
                        Object ret = method.invoke(ioc.getBean(clazz), args);
                        ioc.registerBean(ret);
                    } catch (ReflectiveOperationException e) {
                        throw new LiekkasException("Register bean failed.", e);
                    }
                });

        // inject bean from pool.
        scanner.findInjectFields()
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
