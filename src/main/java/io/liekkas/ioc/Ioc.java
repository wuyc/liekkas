package io.liekkas.ioc;

public interface Ioc {

    void registerBean(Object bean);

    void registerBean(Class<?> beanClass);

    Object getBean(String name);

    <T> T getBean(Class<T> beanClass);

    void removeBean(Class<?> beanClass);

    void removeBean(String name);

    void clearAll();

}
