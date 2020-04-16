package io.liekkas.util;

import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PathUtil {

    private static final String SLASH = "/";

    private PathUtil() {}

    public static String fixPath(String path) {
        if (path == null) {
            return "/";
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    @SneakyThrows
    public static String getRelativePath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        path = path.substring(contextPath.length());

        if (path.length() > 0) {
            path = path.substring(1);
        }
        if (!path.startsWith(SLASH)) {
            path = SLASH + path;
        }
        return URLDecoder.decode(path, "UTF-8");
    }

}
