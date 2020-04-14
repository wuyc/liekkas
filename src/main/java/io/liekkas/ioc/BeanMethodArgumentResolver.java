package io.liekkas.ioc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanMethodArgumentResolver {

    public Object[] resolveArgument(Class<?>[] methodArgs) {
        LiekkasIoc ioc = LiekkasIoc.getInstance();
        int argsLen = methodArgs.length;
        Object[] args = new Object[argsLen];
        for (int i = 0; i < argsLen; i++) {
            Object bean = ioc.getBean(methodArgs[i]);
            if (null == bean) {
                log.error("Inject bean [{}] failed.", methodArgs[i].getName());
            }
            args[i] = bean;
        }
        return args;
    }

}
