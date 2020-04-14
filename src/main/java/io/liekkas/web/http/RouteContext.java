package io.liekkas.web.http;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RouteContext {

    @Getter
    private HttpServletRequest request;
    @Getter
    private HttpServletResponse response;

    private RouteContext() {}

    private static final ThreadLocal<RouteContext> CONTEXT = new ThreadLocal<RouteContext>();

    public static void init(HttpServletRequest req, HttpServletResponse resp) {
        RouteContext context = new RouteContext();
        context.request = req;
        context.response = resp;
        CONTEXT.set(context);
    }

    public static RouteContext get() {
        return CONTEXT.get();
    }

    public static void remove() {
        CONTEXT.remove();
    }

}
