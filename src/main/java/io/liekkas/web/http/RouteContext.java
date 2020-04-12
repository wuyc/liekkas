package io.liekkas.web.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@AllArgsConstructor
public class RouteContext {

    @Getter
    private HttpServletRequest request;
    @Getter
    private HttpServletResponse response;

}
