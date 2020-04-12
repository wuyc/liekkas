package io.liekkas.web.handler;

import io.liekkas.web.http.RouteContext;

import java.lang.reflect.Method;

@FunctionalInterface
public interface RouteHandler {

    Method METHOD = RouteHandler.class.getDeclaredMethods()[0];

    void handle(RouteContext context);

}
