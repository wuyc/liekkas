package io.liekkas.web.http;

import lombok.SneakyThrows;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.util.*;

public interface Request {

    HttpServletRequest raw();

    <T>T bindWithBody(Class<T> modelClass);

    <T>T bindWithForm(Class<T> modelClass);

//    Integer	pathInt(String name);
//
//    Long pathLong(String name);
//
//    String pathString(String name);
//
//    Map<String, String>	pathParams();

    default Object attribute(String name) {
        return raw().getAttribute(name);
    }

    default Map<String, Object> attributes() {
        Map<String, Object> attributes = new HashMap<>();
        Enumeration<String> names = raw().getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Object val = raw().getAttribute(name);
            attributes.put(name, val);
        }
        return attributes;
    }

    default String contextPath() {
        return raw().getContextPath();
    }
    default List<Cookie> cookies() {
        return Arrays.asList(raw().getCookies());
    }

    default Cookie cookie(String name) {
        Cookie[] cookies = raw().getCookies();
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    @SneakyThrows
    default Part file(String name) {
        return raw().getPart(name);
    }

    @SneakyThrows
    default Collection<Part> files() {
        return raw().getParts();
    }

    default String header(String name) {
        return raw().getHeader(name);
    }

    default Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> names = raw().getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String header = raw().getHeader(name);
            headers.put(name, header);
        }
        return headers;
    }

    default HttpMethod httpMethod() {
        String method = raw().getMethod();
        return HttpMethod.valueOf(method);
    }

    default Set<String> parameterNames() {
        Set<String> ret = new HashSet<>();
        Enumeration<String> names = raw().getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            ret.add(name);
        }
        return ret;
    }

    default List<String> parameterValues(String paramName) {
        String[] values = raw().getParameterValues(paramName);
        return Arrays.asList(values);
    }

    default Map<String, List<String>> parameters() {
        Map<String, String[]> paramMap = raw().getParameterMap();
        Map<String, List<String>> retMap = new HashMap<>();
        paramMap.forEach((k, v) -> {
            retMap.put(k, Arrays.asList(v));
        });
        return retMap;
    }

    default String parameter(String paramName) {
        return raw().getParameter(paramName);
    }

    default Optional<String> query(String name) {
        String[] queryArr = raw().getQueryString().split("&");
        for (String str : queryArr) {
            String left = str.split("=")[0];
            String right = str.split("=")[1];
            if (left.equals(name)) {
                return Optional.of(right);
            }
        }
        return Optional.empty();
    }

    default Optional<Boolean> queryBoolean(String name) {
        return query(name).map(Boolean::valueOf);
    }

    default Optional<Double> queryDouble(String name) {
        return query(name).map(Double::valueOf);
    }

    default Optional<Integer> queryInt(String name) {
        return query(name).map(Integer::valueOf);
    }

    default Optional<Long> queryLong(String name) {
        return query(name).map(Long::valueOf);
    }

    default Optional<String> queryString() {
        return Optional.of(raw().getQueryString());
    }

    default HttpSession session() {
        return raw().getSession();
    }

    default HttpSession session(boolean create) {
        return raw().getSession(create);
    }

    default String uri() {
        return raw().getRequestURI();
    }

    default String url() {
        return raw().getRequestURL().toString();
    }

}
