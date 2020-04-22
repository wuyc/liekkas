package io.liekkas.web.http;

import com.google.gson.Gson;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpRequest implements Request {

    private HttpServletRequest raw;
    private Gson gson;

    private HttpRequest() {
    }

    public HttpRequest(HttpServletRequest raw) {
        this.raw = raw;
        this.gson = new Gson();
    }

    @Override
    public HttpServletRequest raw() {
        return raw;
    }

    @SneakyThrows
    @Override
    public <T> T bindWithBody(Class<T> modelClazz) {
        return gson.fromJson(raw().getReader(), modelClazz);
    }

    @Override
    public <T> T bindWithForm(Class<T> modelClazz) {
        Map<String, String> paramMap = new HashMap<>();
        Set<String> paramNames = this.parameterNames();
        for (String name : paramNames) {
            String val = this.parameter(name);
            paramMap.put(name, val);
        }
        String json = gson.toJson(paramMap);
        return gson.fromJson(json, modelClazz);
    }

    /**
     ******************************************************
     * (TODO) These methods operate the REST API Route.   *
     ******************************************************
     */

//    @Override
//    public Integer pathInt(String name) {
//        return null;
//    }
//
//    @Override
//    public Long pathLong(String name) {
//        return null;
//    }
//
//    @Override
//    public String pathString(String name) {
//        return null;
//    }
//
//    @Override
//    public Map<String, String> pathParams() {
//        return null;
//    }

}
