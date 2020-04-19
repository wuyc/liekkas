package io.liekkas.web.annotation;

import io.liekkas.web.http.HttpMethod;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeleteRoute {

    String value() default "/";

}
