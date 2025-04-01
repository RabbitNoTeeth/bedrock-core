# bedrock-core

bedrock-core is a toolkit based on vert.x for java application development, you can use it to easily create one or multi asynchronous and flexible tcp/udp/http server in your java application.

## Table of content

- [Installation](#installation)
- [Usage](#usage)
- [License](#license)

## Installation

### Using Maven

Add the following dependency to your pom.xml:

```xml
    <dependency>
        <groupId>com.gitee.rabbitnoteeth</groupId>
        <artifactId>bedrock-core</artifactId>
        <version>1.0.0</version>
    </dependency>
```

### Using Gradle

Add the following dependency to your build.gradle file:

    compile group: 'com.gitee.rabbitnoteeth', name: 'bedrock-core', version: '1.0.0'

## Usage

### Create tcp server

1. **(required)** define an implement of interface `TcpServerHandler` to handler these events: connect establish、connect disestablish、error、receive data.

```java
public class MyTcpServerHandler implements TcpServerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTcpServerHandler.class);

    @Override
    public void onConnect(NetSocket socket) {
        LOGGER.info("connect established");
    }

    @Override
    public void onDisconnect(NetSocket socket) {
        LOGGER.info("connect disestablished");
    }

    @Override
    public void onError(Throwable e, NetSocket socket) {
        LOGGER.error("error", e);
    }

    @Override
    public void onData(Buffer buffer, NetSocket socket) {
        LOGGER.error("receive data, {}", buffer.toString());
    }
}
```

2. **(optional)** define an implement of interface `ServerDeploymentHandler` to handle these events: server start、server stop.

```java
public class MyTcpServerDeploymentHandler implements ServerDeploymentHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTcpServerDeploymentHandler.class);

    @Override
    public void onStart(String serverId) {
        LOGGER.info("server start");
    }

    @Override
    public void onStartFailed(String serverId, Throwable e) {
        LOGGER.error("server start failed", e);
    }

    @Override
    public void onStop(String serverId) {
        LOGGER.info("server stop");
    }

    @Override
    public void onStopFailed(String serverId, Throwable e) {
        LOGGER.error("server stop failed", e);
    }
}
```

3. create server
```java
// create options (this is optional)
NetServerOptions options = new NetServerOptions();
// config the options...

// create server
TcpServer server = new TcpServerBuilder(8080)
    .setHandler(new MyTcpServerHandler())
    .setDeploymentHandler(new MyTcpServerDeploymentHandler())
    .setOptions(options)
    .build();

// start server
server.start();
```

### Create udp server

1. **(required)** define an implement of interface `UcpServerHandler` to handler these events: connect establish、connect disestablish、error、receive data.

```java
public class MyUdpServerHandler implements UdpServerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyUdpServerHandler.class);

    @Override
    public void onConnect(DatagramSocket socket) {
        LOGGER.info("connect established");
    }

    @Override
    public void onDisconnect(DatagramSocket socket) {
        LOGGER.info("connect disestablished");
    }

    @Override
    public void onError(Throwable e, DatagramSocket socket) {
        LOGGER.error("error", e);
    }

    @Override
    public void onData(DatagramPacket packet, DatagramSocket socket) {
        LOGGER.error("receive data, {}", packet.toString());
    }
}
```

2. **(optional)** define an implement of interface `ServerDeploymentHandler` to handle these events: server start、server stop.

```java
public class MyUdpServerDeploymentHandler implements ServerDeploymentHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyUdpServerDeploymentHandler.class);

    @Override
    public void onStart(String serverId) {
        LOGGER.info("server start");
    }

    @Override
    public void onStartFailed(String serverId, Throwable e) {
        LOGGER.error("server start failed", e);
    }

    @Override
    public void onStop(String serverId) {
        LOGGER.info("server stop");
    }

    @Override
    public void onStopFailed(String serverId, Throwable e) {
        LOGGER.error("server stop failed", e);
    }
}
```

3. create server
```java
// create options (this is optional)
DatagramSocketOptions options = new DatagramSocketOptions();
// config the options...

// create server
UdpServer server = new UdpServerBuilder(8080)
    .setHandler(new MyUdpServerHandler())
    .setDeploymentHandler(new MyTcpServerDeploymentHandler())
    .setOptions(options)
    .build();

// start server
server.start();
```

### Create http server

you may need the server return json data or html data (which rendered by a template engine such as `thymeleaf` ), well, both of them are supported in bedrock-core. Next I'll show you how to use it.



1. if you want to return json data, then you should define an implement of interface `HttpRestRoute` (no methods are defined in this interface, it just means that your implement of it is a rest route) .

```java
@RoutePath("/demo")
public class RestRouteDemo implements HttpRestRoute {

    @RoutePath("/test")
    public String test(HttpContext httpContext,
                    HttpSession httpSession,
                    HttpRequestParams requestParams,
                    HttpRequestHeaders httpRequestHeaders,
                    HttpRequestBody httpRequestBody,
                    @RequestHeader("User-Agent") String userAgent,
                    @RequestParam("asd") String asd,
                    @RequestBody String xxx,
                    @RequestBody(attr = "aaa") String aaa,
                    @RequestBody(attr = "bbb", isArray = true) List<String> bbb,
                    @RequestData Aaa data) {
        System.out.println("RequestHeader: User-Agent=" + userAgent);
        System.out.println("RequestParam: asd=" + asd);
        System.out.println("RequestBody: " + xxx);
        System.out.println("RequestBody: aaa=" + aaa);
        System.out.println("RequestBody: bbb=" + bbb);
        System.out.println("RequestData: data=" + data);
        return "ok";
    }

}

```

in the `RestRouteDemo`,  the annotation `@RoutePath` define the route path, in this demo, means the request of path `/demo/test`  will be handle by the method `RestRouteDemo.test` .

if the type of the method parameter are `HttpContext` 、`HttpSession` 、`HttpRequestParams` 、`HttpRequestHeaders` 、`HttpRequestBody` ，the framework will create a instance of it automatically, you can get kinds of data in this request.

in addition, there are some annotations for the method parameter, such as `@RequestHeader` 、`@RequestParam` 、`@RequestBody` 、`HttpRequestParams` 、`@RequestData`, you can use them to get the data from the request.



2. if you want to return html data which rendered by an engine, then you should define an implement of interface `HttpRestRoute` (no methods are defined in this interface, it just means that your implement of it is a template route) .

```
@RoutePath("/demo")
public class TemplateRouteDemo implements HttpTemplateRoute {

    @RoutePath("/index")
    public String index(HttpContext httpContext,
                    HttpSession httpSession,
                    HttpRequestParams requestParams,
                    HttpRequestHeaders httpRequestHeaders,
                    HttpRequestBody httpRequestBody,
                    @RequestHeader("User-Agent") String userAgent,
                    @RequestParam("asd") String asd,
                    @RequestBody String xxx,
                    @RequestBody(attr = "aaa") String aaa,
                    @RequestBody(attr = "bbb", isArray = true) List<String> bbb,
                    @RequestData Aaa data) {
        System.out.println("RequestHeader: User-Agent=" + userAgent);
        System.out.println("RequestParam: asd=" + asd);
        System.out.println("RequestBody: " + xxx);
        System.out.println("RequestBody: aaa=" + aaa);
        System.out.println("RequestBody: bbb=" + bbb);
        System.out.println("RequestData: data=" + data);
        context.put("content", "this is index");
        return "index";
    }

}
```

in the `TemplateRouteDemo`,  the annotation `@RoutePath` define the route path, in this demo, means the request of path `/demo/index`  will be handle by the method `TemplateRouteDemo.index` , and this will return html data renderer by template `index.html`.

if the type of the method parameter are `HttpContext` 、`HttpSession` 、`HttpRequestParams` 、`HttpRequestHeaders` 、`HttpRequestBody` ，the framework will create a instance of it automatically, you can get kinds of data in this request.

in addition, there are some annotations for the method parameter, such as `@RequestHeader` 、`@RequestParam` 、`@RequestBody` 、`HttpRequestParams` 、`@RequestData`, you can use them to get the data from the request.



3. create server
```java
HttpServer server = new HttpServerBuilder(8090)
    .setDeploymentOptions(deploymentOptions)
    .setRestRoutes(List.of(new RestRouteDemo()))
    .setTemplateRoutes(List.of(new TemplateRouteDemo()))
    .build();

// start server
server.start();
```



4. about more
   - bedrock-core support two kinds of template engine: `thymeleaf`  、 `freemaker`, you can set by the method `HttpServerBuilder.setTemplateEngine`,  `thymeleaf` is default.
   - you can set a global exception handler by `HttpServerBuilder.setErrorHandler`.
   - you can add request handlers by  `HttpServerBuilder.setRequestHandlers`, the request handler will execute before your route, you can do some work in it like check whether requests are too frequent.
   - you can add response handlers by  `HttpServerBuilder.setResponseHandlers`, the response handler will execute after your route, you can do some work in it like wrap the data in a uniform format.
   - you can add route interceptors by   `HttpServerBuilder.setRestRouteInterceptors`, you can do some work before or after the route, like check whether the user is logged in .





## License

Published under MIT License, see LICENSE
