package io.liekkas.ioc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeanEntity {

    private Object bean;
    private Class<?> type;

    public BeanEntity(Object bean) {
        this.bean = bean;
        this.type = bean.getClass();
    }

    public BeanEntity(Class<?> type) {
        try {
            this.type = type;
            this.bean = type.newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

}
