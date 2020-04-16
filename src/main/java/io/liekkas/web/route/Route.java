package io.liekkas.web.route;

import io.liekkas.web.http.HttpMethod;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class Route implements Cloneable {

    private HttpMethod httpMethod;
    private String path;
    private Method action;
    private Object controller;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
