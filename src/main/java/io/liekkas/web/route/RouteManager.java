package io.liekkas.web.route;

import io.liekkas.exception.LiekkasException;
import io.liekkas.ioc.Ioc;
import io.liekkas.ioc.LiekkasIoc;
import io.liekkas.util.ClassScanner;
import io.liekkas.util.PathUtil;
import io.liekkas.web.annotation.*;
import io.liekkas.web.http.HttpMethod;
import lombok.Builder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RouteManager {

    public static void init(String packageName) {
        Ioc ioc = LiekkasIoc.getInstance();
        ClassScanner scanner = new ClassScanner(packageName);
        scanner.scanRoutes()
                .forEach(action -> {
                    Class<?> clazz = action.getDeclaringClass();
                    Object controller = ioc.getBean(clazz);
                    Route clazzRoute = clazz.getAnnotation(Route.class);
                    if (null == clazzRoute) {
                        throw new LiekkasException("Please add @Route to web controller.");
                    }
                    handleRoute(clazzRoute, action, controller);
                });
    }

    private static void handleRoute(Route clazzRoute, Method action, Object controller) {
        String controllerPath = clazzRoute.value();
        String fixedPath = "/".equals(PathUtil.fixPath(controllerPath)) ? "" : PathUtil.fixPath(controllerPath);

        RouteStruct.RouteStructBuilder clazzRouteStruct = RouteStruct
                .builder()
                .path(fixedPath)
                .httpMethods(clazzRoute.method());

        Route route = action.getAnnotation(Route.class);
        if (null != route) {
            RouteStruct.RouteStructBuilder actionRouteStruct = RouteStruct
                    .builder()
                    .path(route.value())
                    .httpMethods(route.method());
            registerRoute(clazzRouteStruct, actionRouteStruct, action, controller);
        }

        GetRoute getRoute = action.getAnnotation(GetRoute.class);
        if (null != getRoute) {
            RouteStruct.RouteStructBuilder actionRouteStruct = RouteStruct
                    .builder()
                    .path(getRoute.value())
                    .httpMethods(new HttpMethod[] { HttpMethod.GET });
            registerRoute(clazzRouteStruct, actionRouteStruct, action, controller);
        }

        PostRoute postRoute = action.getAnnotation(PostRoute.class);
        if (null != postRoute) {
            RouteStruct.RouteStructBuilder actionRouteStruct = RouteStruct
                    .builder()
                    .path(postRoute.value())
                    .httpMethods(new HttpMethod[] { HttpMethod.POST });
            registerRoute(clazzRouteStruct, actionRouteStruct, action, controller);
        }

        PutRoute putRoute = action.getAnnotation(PutRoute.class);
        if (null != putRoute) {
            RouteStruct.RouteStructBuilder actionRouteStruct = RouteStruct
                    .builder()
                    .path(putRoute.value())
                    .httpMethods(new HttpMethod[] { HttpMethod.PUT });
            registerRoute(clazzRouteStruct, actionRouteStruct, action, controller);
        }

        PatchRoute patchRoute = action.getAnnotation(PatchRoute.class);
        if (null != patchRoute) {
            RouteStruct.RouteStructBuilder actionRouteStruct = RouteStruct
                    .builder()
                    .path(patchRoute.value())
                    .httpMethods(new HttpMethod[] { HttpMethod.PATCH });
            registerRoute(clazzRouteStruct, actionRouteStruct, action, controller);
        }

        DeleteRoute deleteRoute = action.getAnnotation(DeleteRoute.class);
        if (null != deleteRoute) {
            RouteStruct.RouteStructBuilder actionRouteStruct = RouteStruct
                    .builder()
                    .path(deleteRoute.value())
                    .httpMethods(new HttpMethod[] { HttpMethod.DELETE });
            registerRoute(clazzRouteStruct, actionRouteStruct, action, controller);
        }
    }

    private static void registerRoute(RouteStruct.RouteStructBuilder clazzRoute,
                                      RouteStruct.RouteStructBuilder actionRoute,
                                      Method action, Object controller) {
        List<HttpMethod> httpMethods = intersectionHttpMethod(clazzRoute.httpMethods, actionRoute.httpMethods);
        for (HttpMethod httpMethod : httpMethods) {
            RouteEntity routeEntity = RouteEntity
                    .builder()
                    .httpMethod(httpMethod)
                    .path(clazzRoute.path + PathUtil.fixPath(actionRoute.path))
                    .action(action)
                    .controller(controller)
                    .build();
            RouteHolder.addRoute(routeEntity);
        }
    }

    private static List<HttpMethod> intersectionHttpMethod(HttpMethod[] clazzHttpMethods, HttpMethod[] actionHttpMethods) {
        List<HttpMethod> clazzHttpMethodList = Arrays.asList(clazzHttpMethods);
        List<HttpMethod> actionHttpMethodList = Arrays.asList(actionHttpMethods);
        if (clazzHttpMethodList.contains(HttpMethod.ALL)) {
            return actionHttpMethodList;
        }
        if (actionHttpMethodList.contains(HttpMethod.ALL)) {
            return clazzHttpMethodList;
        }
        clazzHttpMethodList.retainAll(actionHttpMethodList);
        return clazzHttpMethodList;
    }

    @Builder
    private static class RouteStruct {
        private String path;
        private HttpMethod[] httpMethods;
    }

}
