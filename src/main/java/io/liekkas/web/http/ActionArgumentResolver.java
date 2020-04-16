package io.liekkas.web.http;

import io.liekkas.exception.LiekkasException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        argMapping.put(HttpServletRequest.class.getName(), context.getRequest());
        argMapping.put(HttpServletResponse.class.getName(), context.getResponse());
    }

    public Object[] resolveArgument(Class<?>[] actionArgs) {
        int argsLen = actionArgs.length;
        Object[] args = new Object[argsLen];
        for (int i = 0; i < argsLen; i++) {
            args[i] = queryArg(actionArgs[i]);
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
