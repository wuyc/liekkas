package io.liekkas.web.route;

import io.liekkas.web.http.HttpMethod;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@Builder
public class RouteEntity {

    private HttpMethod httpMethod;
    private String path;
    private Method action;
    private Object controller;

}
