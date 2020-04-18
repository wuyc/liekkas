package io.liekkas.ioc;

import io.liekkas.exception.LiekkasException;
import io.liekkas.ioc.entity.BeanEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LiekkasIoc implements Ioc {

    private final Map<String, BeanEntity> beanPool = new ConcurrentHashMap<>(32);

    private static final class SingletonHolder {
        private static final LiekkasIoc liekkasIoc = new LiekkasIoc();
    }

    public static LiekkasIoc getInstance() {
        return SingletonHolder.liekkasIoc;
    }

    @Override
    public void registerBean(Object bean) {
        BeanEntity beanEntity = new BeanEntity(bean);
        Class<?> type = beanEntity.getType();
        put(type.getName(), beanEntity);
        Class<?>[] interfaces = type.getInterfaces();
        for (Class<?> interfaceClazz : interfaces) {
            put(interfaceClazz.getName(), beanEntity);
        }
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
            throw new LiekkasException("Get bean failed.", e);
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
