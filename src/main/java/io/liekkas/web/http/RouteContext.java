package io.liekkas.web.http;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RouteContext {

    @Getter
    private HttpServletRequest rawRequest;
    @Getter
    private HttpServletResponse rawResponse;
    @Getter
    private Request request;

    private RouteContext() {}

    private static final ThreadLocal<RouteContext> CONTEXT = new ThreadLocal<>();

    public static void init(HttpServletRequest req, HttpServletResponse resp) {
        RouteContext context = new RouteContext();
        context.rawRequest = req;
        context.rawResponse = resp;
        context.request = new HttpRequest(req);
        CONTEXT.set(context);
    }

    public static RouteContext get() {
        return CONTEXT.get();
    }

    public static void remove() {
        CONTEXT.remove();
    }

}
