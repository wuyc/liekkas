package io.liekkas.web.http;

import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;

public class HttpResponse implements Response {

    private HttpServletResponse raw;

    private HttpResponse() {
    }

    public HttpResponse(HttpServletResponse raw) {
        this.raw = raw;
    }

    @Override
    public HttpServletResponse raw() {
        return raw;
    }

    @Override
    public void badRequest() {
        this.status(400);
    }

    @Override
    public void unauthorized() {
        this.status(400);
    }

    @Override
    public void notFound() {
        this.status(404);
    }

    @SneakyThrows
    @Override
    public void text(String text) {
        this.contentType("text/plain; charset=UTF-8");
        raw().getWriter().write(text);
    }

    @SneakyThrows
    @Override
    public void html(String html) {
        this.contentType("text/html; charset=UTF-8");
        raw().getWriter().write(html);
    }

    @SneakyThrows
    @Override
    public void json(String json) {
        this.contentType("application/json; charset=UTF-8");
        raw().getWriter().write(json);
    }

    /**
     *********************************
     * TODO                          *
     *********************************
     */
    @Override
    public void render(String view) {

    }

}
