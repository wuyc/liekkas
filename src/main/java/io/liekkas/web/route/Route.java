package io.liekkas.web.route;

import io.liekkas.web.http.HttpMethod;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class Route {

    private HttpMethod httpMethod;
    private String path;
    private Method action;
    private Object controller;

}
