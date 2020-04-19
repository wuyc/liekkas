package io.liekkas.web.route;

import io.liekkas.util.PathUtil;
import io.liekkas.web.http.HttpMethod;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RouteHolder {

    @Getter
    private static Map<String, RouteEntity> routeMapping = new HashMap<>();

    private RouteHolder() {}

    public static void addRoute(List<RouteEntity> routeEntities) {
        for (RouteEntity routeEntity : routeEntities) {
            addRoute(routeEntity);
        }
    }

    @SneakyThrows
    public static void addRoute(RouteEntity routeEntity) {
        if (!routeEntity.getHttpMethod().equals(HttpMethod.ALL)) {
            String key = genRouteKey(routeEntity.getPath(), routeEntity.getHttpMethod());
            routeMapping.putIfAbsent(key, routeEntity);
        } else {
            for (HttpMethod method : HttpMethod.values()) {
                RouteEntity clone = (RouteEntity) routeEntity.clone();
                clone.setHttpMethod(method);
                String key = genRouteKey(routeEntity.getPath(), method);
                routeMapping.putIfAbsent(key, routeEntity);
            }
        }

    }

    public static RouteEntity findRoute(String path, HttpMethod httpMethod) {
        String key = genRouteKey(path, httpMethod);
        return routeMapping.get(key);
    }

    public static RouteEntity findRoute(String path, String httpMethod) {
        return findRoute(path, HttpMethod.valueOf(httpMethod));
    }

    private static String genRouteKey(String path, HttpMethod httpMethod) {
        return PathUtil.fixPath(path) + "#" + httpMethod.toString();
    }

}
