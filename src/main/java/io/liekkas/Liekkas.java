package io.liekkas;

import io.liekkas.util.PathUtil;
import io.liekkas.web.handler.RouteHandler;
import io.liekkas.web.http.HttpMethod;
import io.liekkas.web.route.RouteEntity;
import io.liekkas.web.route.RouteHolder;

public class Liekkas {

    private Liekkas() {}

    private static final class SingletonHolder {
        private static final Liekkas liekkas = new Liekkas();
    }

    public static Liekkas getInstance() {
        return SingletonHolder.liekkas;
    }

    public Liekkas get(String path, RouteHandler routeHandler) {
        RouteEntity routeEntity = RouteEntity
                .builder()
                .httpMethod(HttpMethod.GET)
                .path(PathUtil.fixPath(path))
                .action(RouteHandler.METHOD)
                .controller(routeHandler)
                .build();
        RouteHolder.addRoute(routeEntity);
        return this;
    }

    public Liekkas post(String path, RouteHandler routeHandler) {
        RouteEntity routeEntity = RouteEntity
                .builder()
                .httpMethod(HttpMethod.POST)
                .path(PathUtil.fixPath(path))
                .action(RouteHandler.METHOD)
                .controller(routeHandler)
                .build();
        RouteHolder.addRoute(routeEntity);
        return this;
    }

    public Liekkas put(String path, RouteHandler routeHandler) {
        RouteEntity routeEntity = RouteEntity
                .builder()
                .httpMethod(HttpMethod.PUT)
                .path(PathUtil.fixPath(path))
                .action(RouteHandler.METHOD)
                .controller(routeHandler)
                .build();
        RouteHolder.addRoute(routeEntity);
        return this;
    }

    public Liekkas patch(String path, RouteHandler routeHandler) {
        RouteEntity routeEntity = RouteEntity
                .builder()
                .httpMethod(HttpMethod.PATCH)
                .path(PathUtil.fixPath(path))
                .action(RouteHandler.METHOD)
                .controller(routeHandler)
                .build();
        RouteHolder.addRoute(routeEntity);
        return this;
    }

    public Liekkas delete(String path, RouteHandler routeHandler) {
        RouteEntity routeEntity = RouteEntity
                .builder()
                .httpMethod(HttpMethod.DELETE)
                .path(PathUtil.fixPath(path))
                .action(RouteHandler.METHOD)
                .controller(routeHandler)
                .build();
        RouteHolder.addRoute(routeEntity);
        return this;
    }

    public Liekkas options(String path, RouteHandler routeHandler) {
        RouteEntity routeEntity = RouteEntity
                .builder()
                .httpMethod(HttpMethod.OPTIONS)
                .path(PathUtil.fixPath(path))
                .action(RouteHandler.METHOD)
                .controller(routeHandler)
                .build();
        RouteHolder.addRoute(routeEntity);
        return this;
    }

    public Liekkas match(Iterable<HttpMethod> httpMethods, String path, RouteHandler routeHandler) {
        for (HttpMethod httpMethod : httpMethods) {
            RouteEntity routeEntity = RouteEntity
                    .builder()
                    .httpMethod(httpMethod)
                    .path(PathUtil.fixPath(path))
                    .action(RouteHandler.METHOD)
                    .controller(routeHandler)
                    .build();
            RouteHolder.addRoute(routeEntity);
        }
        return this;
    }

    public Liekkas any(String path, RouteHandler routeHandler) {
        RouteEntity routeEntity = RouteEntity
                .builder()
                .httpMethod(HttpMethod.ALL)
                .path(PathUtil.fixPath(path))
                .action(RouteHandler.METHOD)
                .controller(routeHandler)
                .build();
        RouteHolder.addRoute(routeEntity);
        return this;
    }

}
