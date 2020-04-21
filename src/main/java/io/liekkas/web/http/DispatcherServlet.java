package io.liekkas.web.http;

import io.liekkas.Liekkas;
import io.liekkas.exception.LiekkasException;
import io.liekkas.ioc.Ioc;
import io.liekkas.ioc.LiekkasIoc;
import io.liekkas.ioc.bean.BeanManager;
import io.liekkas.util.PathUtil;
import io.liekkas.Bootstrap;
import io.liekkas.web.route.RouteEntity;
import io.liekkas.web.route.RouteHolder;
import io.liekkas.web.route.RouteManager;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        String basePackage = getInitParameter("base-package");
        // init ioc
        BeanManager.init(basePackage);
        Bootstrap bootstrap = newBootstrap(getInitParameter("bootstrap"));
        // init application
        bootstrap.init(Liekkas.getInstance());
        // init route
        RouteManager.init(basePackage);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = PathUtil.getRelativePath(req);
        RouteEntity routeEntity = RouteHolder.findRoute(uri, req.getMethod());
        RouteContext.init(req, resp);
        if (null == routeEntity) {
            render404(resp);
        } else {
            handleReq(routeEntity);
        }
        RouteContext.remove();
    }

    private Bootstrap newBootstrap(String className) {
        Ioc ioc = LiekkasIoc.getInstance();
        Bootstrap bootstrap = (Bootstrap) ioc.getBean(className);
        if (null == bootstrap) {
            ioc.registerBean(className);
            bootstrap = (Bootstrap) ioc.getBean(className);
        }
        return bootstrap;
    }

    private void render404(HttpServletResponse resp) throws IOException {
        resp.setStatus(404);
        resp.getWriter().write("404 NOT FOUND");
    }

    private void handleReq(RouteEntity routeEntity) {
        Object controller = routeEntity.getController();
        Method action = routeEntity.getAction();

        action.setAccessible(true);
        Parameter[] actionParams = action.getParameters();

        ActionArgumentResolver resolver = new ActionArgumentResolver();
        Object[] args = resolver.resolveArgument(actionParams);
        try {
            action.invoke(controller, args);
        } catch (ReflectiveOperationException e) {
            throw new LiekkasException("Invoke action failed.", e);
        }
    }

}
