package io.liekkas.ioc;

import io.liekkas.exception.BeanException;
import io.liekkas.ioc.entity.BeanEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LiekkasIoc implements Ioc {

    private static LiekkasIoc liekkasIoc;
    private final Map<String, BeanEntity> beanPool = new ConcurrentHashMap<>(32);

    public static LiekkasIoc getInstance() {
        if (null == liekkasIoc) {
            synchronized (LiekkasIoc.class) {
                if (null == liekkasIoc) {
                    liekkasIoc = new LiekkasIoc();
                }
            }
        }
        return liekkasIoc;
    }

    @Override
    public void registerBean(Object bean) {
        registerBean(bean.getClass());
    }

    @Override
    public void registerBean(Class<?> beanClass) {
        BeanEntity beanEntity = new BeanEntity(beanClass);
        put(beanClass.getName(), beanEntity);
        Class<?>[] interfaces = beanClass.getInterfaces();
        for (Class<?> interfaceClazz : interfaces) {
            put(interfaceClazz.getName(), beanEntity);
        }
    }

    @Override
    public Object getBean(String name) {
        BeanEntity beanEntity = beanPool.get(name);
        if (null == beanEntity) {
            return null;
        }
        return beanEntity.getBean();
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
        Object bean = getBean(beanClass.getName());
        try {
            return beanClass.cast(bean);
        } catch (ClassCastException e) {
            throw new BeanException("Get bean failed.", e);
        }
    }

    @Override
    public void removeBean(Class<?> beanClass) {
        removeBean(beanClass.getName());
    }

    @Override
    public void removeBean(String name) {
        beanPool.remove(name);
    }

    @Override
    public void clearAll() {
        beanPool.clear();
    }

    private void put(String name, BeanEntity beanEntity) {
        if (null != beanPool.put(name, beanEntity)) {
            log.warn("Duplicated Bean: {}", name);
        }
    }

}
