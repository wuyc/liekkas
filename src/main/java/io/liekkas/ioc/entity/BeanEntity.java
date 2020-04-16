package io.liekkas.ioc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

@Data
@AllArgsConstructor
public class BeanEntity {

    private Object bean;
    private Class<?> type;

    public BeanEntity(Object bean) {
        this.bean = bean;
        this.type = bean.getClass();
    }

    @SneakyThrows
    public BeanEntity(Class<?> type) {
        this.type = type;
        this.bean = type.newInstance();
    }

}
