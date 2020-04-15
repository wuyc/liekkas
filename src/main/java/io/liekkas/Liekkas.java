package io.liekkas;

import io.liekkas.web.handler.RouteHandler;
import io.liekkas.web.http.HttpMethod;
import io.liekkas.web.route.RouteHolder;

public class Liekkas {

    private static Liekkas liekkas;

    private Liekkas() {}

    public static Liekkas newInstance() {
        if (null == liekkas) {
            synchronized (Liekkas.class) {
                if (null == liekkas) {
                    liekkas = new Liekkas();
                }
            }
        }
        return liekkas;
    }

    public Liekkas get(String path, RouteHandler routeHandler) {
        RouteHolder.addRoute(HttpMethod.GET, path, RouteHandler.METHOD, routeHandler);
        return this;
    }

    public Liekkas post(String path, RouteHandler routeHandler) {
        RouteHolder.addRoute(HttpMethod.POST, path, RouteHandler.METHOD, routeHandler);
        return this;
    }

    public Liekkas put(String path, RouteHandler routeHandler) {
        RouteHolder.addRoute(HttpMethod.PUT, path, RouteHandler.METHOD, routeHandler);
        return this;
    }

    public Liekkas patch(String path, RouteHandler routeHandler) {
        RouteHolder.addRoute(HttpMethod.PATCH, path, RouteHandler.METHOD, routeHandler);
        return this;
    }

    public Liekkas delete(String path, RouteHandler routeHandler) {
        RouteHolder.addRoute(HttpMethod.DELETE, path, RouteHandler.METHOD, routeHandler);
        return this;
    }

    public Liekkas options(String path, RouteHandler routeHandler) {
        RouteHolder.addRoute(HttpMethod.OPTIONS, path, RouteHandler.METHOD, routeHandler);
        return this;
    }

    public Liekkas match(Iterable<HttpMethod> httpMethods, String path, RouteHandler routeHandler) {
        for (HttpMethod httpMethod : httpMethods) {
            RouteHolder.addRoute(httpMethod, path, RouteHandler.METHOD, routeHandler);
        }
        return this;
    }

    public Liekkas any(String path, RouteHandler routeHandler) {
        RouteHolder.addRoute(HttpMethod.ALL, path, RouteHandler.METHOD, routeHandler);
        return this;
    }

}
