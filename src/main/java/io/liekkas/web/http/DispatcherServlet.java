package io.liekkas.web.http;

import io.liekkas.Liekkas;
import io.liekkas.exception.LiekkasException;
import io.liekkas.ioc.bean.BeanManager;
import io.liekkas.ioc.LiekkasIoc;
import io.liekkas.util.PathUtil;
import io.liekkas.web.Bootstrap;
import io.liekkas.web.route.Route;
import io.liekkas.web.route.RouteHolder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@Slf4j
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        BeanManager.init(getInitParameter("base-package"));
        String className = getInitParameter("bootstrap");
        LiekkasIoc ioc = LiekkasIoc.getInstance();
        ioc.registerBean(className);
        Bootstrap bootstrap = (Bootstrap) ioc.getBean(className);
        bootstrap.init(Liekkas.newInstance());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = PathUtil.getRelativePath(req);
        Route route = RouteHolder.findRoute(uri, req.getMethod());
        RouteContext.init(req, resp);
        if (null == route) {
            resp.setStatus(404);
            resp.getWriter().write("404 NOT FOUND");
        } else {
            handle(route);
        }
        RouteContext.remove();
    }

    private void handle(Route route) {
        Object controller = route.getController();
        Method action = route.getAction();

        action.setAccessible(true);
        Class<?>[] methodParams = action.getParameterTypes();

        ActionArgumentResolver resolver = new ActionArgumentResolver();
        Object[] args = resolver.resolveArgument(methodParams);
        try {
            action.invoke(controller, args);
        } catch (ReflectiveOperationException e) {
            throw new LiekkasException("Invoke action failed.", e);
        }
    }

}
