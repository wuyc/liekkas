package io.liekkas.web.route;

import io.liekkas.util.PathUtil;
import io.liekkas.web.http.HttpMethod;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RouteHolder {

    @Getter
    private static Map<String, Route> routeMapping = new HashMap<>();

    private RouteHolder() {}

    public static void addRoute(List<Route> routes) {
        for (Route route : routes) {
            addRoute(route);
        }
    }

    public static void addRoute(Route route) {
        String key = genRouteKey(route.getPath(), route.getHttpMethod());
        routeMapping.putIfAbsent(key, route);
    }

    @SneakyThrows
    public static void addRoute(HttpMethod httpMethod, String path, Method action, Object controller){
        Route route = new Route();
        route.setHttpMethod(httpMethod);
        route.setPath(PathUtil.fixPath(path));
        route.setAction(action);
        route.setController(controller);
        if (!httpMethod.equals(HttpMethod.ALL)) {
            addRoute(route);
        } else {
            for (HttpMethod method : HttpMethod.values()) {
                Route clone = (Route) route.clone();
                clone.setHttpMethod(method);
                addRoute(clone);
            }
        }
    }

    public static Route findRoute(String path, HttpMethod httpMethod) {
        String key = genRouteKey(path, httpMethod);
        return routeMapping.get(key);
    }

    public static Route findRoute(String path, String httpMethod) {
        return findRoute(path, HttpMethod.valueOf(httpMethod));
    }

    private static String genRouteKey(String path, HttpMethod httpMethod) {
        return PathUtil.fixPath(path) + "#" + httpMethod.toString();
    }

}
