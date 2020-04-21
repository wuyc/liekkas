package io.liekkas.web.http;

import io.liekkas.exception.LiekkasException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ActionArgumentResolver {

    private Map<String, Object> argMapping = new HashMap<>(3);

    public ActionArgumentResolver() {
        RouteContext context = RouteContext.get();
        if (null == context) {
            throw new LiekkasException("Context is not existent.");
        }
        argMapping.put(RouteContext.class.getName(), context);
        argMapping.put(HttpServletRequest.class.getName(), context.getRawRequest());
        argMapping.put(HttpServletResponse.class.getName(), context.getRawResponse());
        argMapping.put(Request.class.getName(), context.getRequest());
        argMapping.put(Response.class.getName(), context.getResponse());
    }

    public Object[] resolveArgument(Parameter[] actionParams) {
        int paramsLen = actionParams.length;
        Object[] args = new Object[paramsLen];
        for (int i = 0; i < paramsLen; i++) {
            args[i] = queryArg(actionParams[i].getType());
        }
        return args;
    }

    private Object queryArg(Class<?> arg) {
        Object obj = argMapping.get(arg.getName());
        if (null == obj) {
            log.warn("Action argument inject failed.");
        }
        return obj;
    }

}
