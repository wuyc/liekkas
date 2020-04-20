package io.liekkas.web.http;

import lombok.SneakyThrows;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public interface Response {

    HttpServletResponse raw();

    void badRequest();

    void unauthorized();

    void notFound();

    void text(String text);

    void html(String html);

    void json(String json);

    void render(String view);

    default void cookie(Cookie cookie) {
        raw().addCookie(cookie);
    }

    default void header(String name, String value) {
        raw().addHeader(name, value);
    }

    default int status() {
        return raw().getStatus();
    }

    default void status(int sc) {
        raw().setStatus(sc);
    }

    @SneakyThrows
    default void error(int sc) {
        raw().sendError(sc);
    }

    @SneakyThrows
    default void error(int sc, String msg) {
        raw().sendError(sc, msg);
    }

    @SneakyThrows
    default void redirect(String location) {
        raw().sendRedirect(location);
    }

    default void contentType(String contentType) {
        raw().setContentType(contentType);
    }

}
