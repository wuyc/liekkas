## 简介

Liekkas 是一个基于 Servlet API 极其简约的 Web 框架，同时也实现了一个容器，仅供学习使用~

Demo: [https://github.com/wuyc/liekkas-demo](https://github.com/wuyc/liekkas-demo)

## 开始使用

1. 配置 web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
		 http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <display-name>Archetype Created Web Application</display-name>

    <servlet>
        <servlet-name>liekkas</servlet-name>
        <servlet-class>io.liekkas.web.http.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>bootstrap</param-name>
            <param-value>io.liekkas.demo.Application</param-value>
        </init-param>
        <init-param>
            <param-name>base-package</param-name>
            <param-value>io.liekkas.demo</param-value>
        </init-param>
        <multipart-config>
            <location>/tmp</location>
            <max-file-size>20848820</max-file-size>
            <max-request-size>418018841</max-request-size>
            <file-size-threshold>1048576</file-size-threshold>
        </multipart-config>
    </servlet>

    <servlet-mapping>
        <servlet-name>liekkas</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>

</web-app>
```

2. 创建启动类

```java
@Bean
public class Application implements Bootstrap {

    @Override
    public void init(Liekkas liekkas) {
        liekkas
                .get("/", context -> {
                    context.getResponse().text("Hello world");
                });
    }

}
```

3. 注解路由

   方法入参可以是：

- `RouteContext`
- `Request`
- `Response`
- `HttpServletRequest`
- `HttpServletResponse`

```java
@Route
public class HelloController {

    @GetRoute("/hello")
    public void test(Request request, Response response) {
        response.text("Hello~");
    }

}
```

4. 容器

```java
@Bean
public class Person {
    
    @Inject
    private Dog dog;

}
```

## 使用建议

Just For Fun.