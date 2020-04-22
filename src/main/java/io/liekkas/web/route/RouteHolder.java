package io.liekkas.web.route;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.liekkas.util.PathUtil;
import io.liekkas.web.http.HttpMethod;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
public class RouteHolder {

    @Getter
    private static Table<String, HttpMethod, RouteEntity> routeMapping = HashBasedTable.create();

    private RouteHolder() {
    }

    public static void addRoute(List<RouteEntity> routeEntities) {
        for (RouteEntity routeEntity : routeEntities) {
            addRoute(routeEntity);
        }
    }

    @SneakyThrows
    public static void addRoute(RouteEntity routeEntity) {
        String path = routeEntity.getPath();
        HttpMethod httpMethod = routeEntity.getHttpMethod();
        if (!httpMethod.equals(HttpMethod.ALL)) {
            routeMapping.put(path, httpMethod, routeEntity);
        } else {
            for (HttpMethod method : HttpMethod.values()) {
                routeMapping.put(path, method, routeEntity);
            }
        }
    }

    public static RouteEntity findRoute(String path, HttpMethod httpMethod) {
        return routeMapping.get(path, httpMethod);
    }

    public static RouteEntity findRoute(String path, String httpMethod) {
        return findRoute(path, HttpMethod.valueOf(httpMethod));
    }

    public static RouteEntity findRoute(HttpServletRequest rawReq) {
        return findRoute(PathUtil.getRelativePath(rawReq), rawReq.getMethod());
    }

}
