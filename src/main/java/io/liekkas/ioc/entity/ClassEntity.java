package io.liekkas.ioc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class ClassEntity {

    private String className;
    private Class<?> clazz;

    public ClassEntity(Class<?> clazz) {
        this.clazz = clazz;
        this.className = clazz.getName();
    }

}
