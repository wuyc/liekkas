package io.liekkas.web.annotation;

import io.liekkas.web.http.HttpMethod;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Route {

    String value() default "/";

    HttpMethod[] method() default HttpMethod.ALL;

}
