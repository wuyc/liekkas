package io.liekkas.ioc.bean;

import io.liekkas.ioc.Ioc;
import io.liekkas.ioc.LiekkasIoc;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;

@Slf4j
public class BeanMethodArgumentResolver {

    public Object[] resolveArgument(Parameter[] methodParams) {
        Ioc ioc = LiekkasIoc.getInstance();
        int paramsLen = methodParams.length;
        Object[] args = new Object[paramsLen];
        for (int i = 0; i < paramsLen; i++) {
            Object bean = ioc.getBean(methodParams[i].getType());
            if (null == bean) {
                log.error("Inject bean [{}] failed.", methodParams[i].getName());
            }
            args[i] = bean;
        }
        return args;
    }

}
