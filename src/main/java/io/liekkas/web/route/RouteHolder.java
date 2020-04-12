package io.liekkas.web.route;

import io.liekkas.util.PathUtil;
import io.liekkas.web.http.HttpMethod;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RouteHolder {

    @Getter
    private static List<Route> routes = new ArrayList<>();

    private RouteHolder() {}

    public static void addRoute(List<Route> list) {
        routes.addAll(list);
    }

    public static void addRoute(Route route) {
        routes.add(route);
    }

    public static void addRoute(HttpMethod httpMethod, String path, Method action, Object controller){
        Route route = new Route();
        route.setHttpMethod(httpMethod);
        route.setPath(path);
        route.setAction(action);
        route.setController(controller);
        routes.add(route);
    }

    public static Route findRoute(String path) {
        String fixedPath = PathUtil.fixPath(path);
        for (Route route : routes) {
            boolean matched = fixedPath.equals(route.getPath());
            if (matched) {
                return route;
            }
        }
        return null;
    }

}
