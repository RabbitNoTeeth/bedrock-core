package com.github.rabbitnoteeth.bedrock.core.server.http;

import com.github.rabbitnoteeth.bedrock.core.server.http.annotation.RoutePath;
import com.github.rabbitnoteeth.bedrock.core.server.http.constant.HttpConstants;
import com.github.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.github.rabbitnoteeth.bedrock.core.server.http.entity.HttpMethod;
import com.github.rabbitnoteeth.bedrock.core.server.http.handler.HttpRequestHandler;
import com.github.rabbitnoteeth.bedrock.core.server.http.handler.HttpResponseHandler;
import com.github.rabbitnoteeth.bedrock.core.server.http.handler.impl.*;
import com.github.rabbitnoteeth.bedrock.core.server.http.route.HttpRouteInterceptor;
import com.github.rabbitnoteeth.bedrock.core.server.http.route.entity.HttpRouteEndpoint;
import com.github.rabbitnoteeth.bedrock.core.server.http.route.exception.HttpRouteEndpointException;
import com.github.rabbitnoteeth.bedrock.util.ExecutorUtils;
import com.github.rabbitnoteeth.bedrock.util.StringUtils;
import com.github.rabbitnoteeth.bedrock.util.function.BrConsumer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class HttpServerVerticle extends AbstractVerticle {

    private static final ExecutorService VIRTUAL_THREAD_EXECUTOR = ExecutorUtils.createVirtualThreadExecutor("bedrock-http-virtual-");

    @Override
    public void start(Promise<Void> promise) throws Exception {
        try {
            JsonObject config = config();
            String ID = config.getString("id");
            HttpServer httpServer = HttpServer.SERVER_MAP.get(ID);
            if (httpServer == null) {
                promise.fail(new IllegalArgumentException(String.format("Could not find HttpServer instance with id '%s'", ID)));
            } else {
                // create cors handler
                CorsHandler corsHandler = new CorsHandler(httpServer.allowCors, httpServer.corsExposeHeaders);
                // create session handler
                SessionPreHandler sessionPreHandler = new SessionPreHandler(httpServer.sessionStore);
                SessionPostHandler sessionPostHandler = new SessionPostHandler(httpServer.sessionStore);
                // create end handler
                EndHandler endHandler = new EndHandler();
                // create fail handler
                FailureHandler failureHandler = new FailureHandler(httpServer.errorHandler);
                // create main router
                Router mainRouter = Router.router(this.vertx);
                // mount static route
                mountStaticRoute(httpServer, mainRouter, corsHandler);
                // mount rest route
                mountRestRoute(httpServer, mainRouter, corsHandler, sessionPreHandler, sessionPostHandler, endHandler);
                // mount template route
                mountTemplateRoute(httpServer, mainRouter, corsHandler, sessionPreHandler, sessionPostHandler, endHandler);
                // mount sockjs route
                mountSockjsRoute(httpServer, mainRouter);
                // add fail handler
                mainRouter.route().failureHandler(failureHandler);
                // create http server
                this.vertx
                    .createHttpServer(httpServer.options)
                    .webSocketHandler(new WebSocketHandler(httpServer.websocketRoutes))
                    .requestHandler(mainRouter)
                    .listen(httpServer.port(), res -> {
                        if (!res.succeeded()) {
                            promise.fail(res.cause());
                        } else {
                            promise.complete();
                        }
                    });
            }
        } catch (Exception e) {
            promise.fail(e);
        }
    }

    /**
     * mount sockjs route
     *
     * @param httpServer
     * @param mainRouter
     */
    private void mountSockjsRoute(HttpServer httpServer, Router mainRouter) {
        httpServer.sockjsRoutes.forEach(ep -> {
            String path = httpServer.sockjsPathPrefix + ep.path();
            SockJSHandlerOptions sockJSHandlerOptions = new SockJSHandlerOptions().setHeartbeatInterval(2000);
            io.vertx.ext.web.handler.sockjs.SockJSHandler sockJSHandler = io.vertx.ext.web.handler.sockjs.SockJSHandler.create(this.vertx, sockJSHandlerOptions);
            mainRouter.route(path).subRouter(sockJSHandler.socketHandler(new SockJSHandler(ep)));
        });
    }

    /**
     * mount template route
     *
     * @param httpServer
     * @param mainRouter
     * @param corsHandler
     * @param sessionPreHandler
     * @param sessionPostHandler
     * @param endHandler
     */
    private void mountTemplateRoute(HttpServer httpServer,
                                    Router mainRouter,
                                    CorsHandler corsHandler,
                                    SessionPreHandler sessionPreHandler,
                                    SessionPostHandler sessionPostHandler,
                                    EndHandler endHandler) {
        io.vertx.ext.web.common.template.TemplateEngine templateEngine;
        // create template handler
        switch (httpServer.templateEngine) {
            case FREEMARKER -> templateEngine = FreeMarkerTemplateEngine.create(this.vertx);
            case THYMELEAF -> templateEngine = ThymeleafTemplateEngine.create(this.vertx);
            case null -> throw new IllegalArgumentException("TemplateEngine is required");
            default ->
                throw new IllegalArgumentException("invalid TemplateEngine of " + httpServer.templateEngine.name());
        }
        TemplateHandler templateHandler = new TemplateHandler(templateEngine, httpServer.templateResourceDir, httpServer.templateContentType);

        Router router = Router.router(this.vertx);
        router
            .route()
            .handler(BodyHandler.create().setDeleteUploadedFilesOnEnd(true));
        List<HttpRouteEndpoint> endpoints = parseRoutes(httpServer.templateRoutes, String.class, httpServer.templateRouteInterceptors);
        for (HttpRouteEndpoint endpoint : endpoints) {
            Route route = router.get(endpoint.getPath());
            // add pre handlers
            httpServer
                .requestHandlers
                .stream()
                .sorted(Comparator.comparingInt(HttpRequestHandler::order))
                .forEach(handler -> route.handler(context -> executeInVirtualThread(context, handler::execute, handler.async())));

            // add endpoint
            route
                .handler(context -> executeInVirtualThread(context, hc -> {
                    try {
                        Object templateFileResult = endpoint.execute(hc);
                        if (!isHttpContextValid(hc)) {
                            return;
                        }
                        if (templateFileResult == null) {
                            context.fail(HttpConstants.RESPONSE_STATUS_404);
                        } else {
                            String templateFile = templateFileResult.toString();
                            String templateFileType = httpServer.templateFileType;
                            if (!templateFile.contains(".") && StringUtils.isNotBlank(templateFileType)) {
                                templateFile += "." + templateFileType;
                            }
                            if (!templateFile.startsWith("/")) {
                                templateFile = "/" + templateFile;
                            }
                            context.put(TemplateHandler.TEMPLATE_FILE_KEY, templateFile);
                        }
                    } catch (Throwable e) {
                        throw new HttpRouteEndpointException(e);
                    }
                }, true))
                .handler(templateHandler);

            // add post handlers
            httpServer
                .responseHandlers
                .stream()
                .sorted(Comparator.comparingInt(HttpResponseHandler::order))
                .forEach(handler -> route.handler(context -> executeInVirtualThread(context, handler::execute, handler.async())));

            // add end handler
            route.handler(sessionPostHandler).handler(endHandler);
        }

        mainRouter
            .route(httpServer.templateResourcePathPrefix + "/*")
            .handler(corsHandler)
            .handler(sessionPreHandler)
            .subRouter(router);
    }

    /**
     * mount static route
     *
     * @param httpServer
     * @param mainRouter
     * @param corsHandler
     */
    private void mountStaticRoute(HttpServer httpServer, Router mainRouter, CorsHandler corsHandler) {
        StaticHandler staticHandler = StaticHandler.create(httpServer.staticResourceDir);
        staticHandler.setIndexPage(httpServer.indexPage);
        mainRouter.route(httpServer.staticResourcePathPrefix + "/*")
            .handler(staticHandler);
    }

    /**
     * mount api route
     *
     * @param httpServer
     * @param mainRouter
     * @param corsHandler
     * @param sessionPreHandler
     * @param sessionPostHandler
     * @param endHandler
     */
    private void mountRestRoute(HttpServer httpServer,
                                Router mainRouter,
                                CorsHandler corsHandler,
                                SessionPreHandler sessionPreHandler,
                                SessionPostHandler sessionPostHandler,
                                EndHandler endHandler) {
        Router router = Router.router(this.vertx);
        router.route().handler(BodyHandler.create().setDeleteUploadedFilesOnEnd(true));
        List<HttpRouteEndpoint> endpoints = parseRoutes(httpServer.restRoutes, null, httpServer.restRouteInterceptors);
        for (HttpRouteEndpoint endpoint : endpoints) {
            String path = endpoint.getPath();
            Route route;
            HttpMethod httpMethod = endpoint.getMethod();
            if (HttpMethod.ANY.equals(httpMethod)) {
                route = router.route(path);
            } else {
                route = router.route(new io.vertx.core.http.HttpMethod(httpMethod.getName()), path);
            }
            // add pre handler
            httpServer
                .requestHandlers
                .stream()
                .sorted(Comparator.comparingInt(HttpRequestHandler::order))
                .forEach(handler -> route.handler(context -> executeInVirtualThread(context, handler::execute, handler.async())));

            // add endpoint
            route.handler(context -> executeInVirtualThread(context, hc -> {
                try {
                    Object result = endpoint.execute(hc);
                    if (!isHttpContextValid(hc)) {
                        return;
                    }
                    hc.setResponseData(result);
                } catch (Throwable e) {
                    throw new HttpRouteEndpointException(e);
                }
            }, true));

            // add post handler
            httpServer
                .responseHandlers
                .stream()
                .sorted(Comparator.comparingInt(HttpResponseHandler::order))
                .forEach(handler -> route.handler(context -> executeInVirtualThread(context, handler::execute, handler.async())));

            // add end handler
            route
                .handler(sessionPostHandler)
                .handler(new RestDataHandler())
                .handler(endHandler);
        }

        mainRouter
            .route(httpServer.restApiPathPrefix + "/*")
            .handler(corsHandler)
            .handler(sessionPreHandler)
            .subRouter(router);
    }

    private void executeInVirtualThread(RoutingContext context, BrConsumer<HttpContext> consumer, boolean async) {
        try {
            Runnable task = () -> {
                try {
                    HttpContext httpContext = new HttpContext(context);
                    consumer.accept(httpContext);
                    if (httpContext.isRedirected()) {
                        return;
                    }
                    if (httpContext.isInterrupted()) {
                        context.end();
                        return;
                    }
                    if (!httpContext.isEnded()) {
                        context.next();
                    }
                } catch (Throwable e) {
                    context.fail(e);
                }
            };
            if (async) {
                VIRTUAL_THREAD_EXECUTOR.execute(task);
            } else {
                task.run();
            }
        } catch (Throwable e) {
            context.fail(e);
        }
    }

    private List<HttpRouteEndpoint> parseRoutes(List<?> routes, Class<?> returnType, List<HttpRouteInterceptor> interceptors) {
        List<HttpRouteEndpoint> endpoints = new ArrayList<>();
        for (Object route : routes) {
            Class<?> routeClass = route.getClass();
            RoutePath routePathOfType = routeClass.getDeclaredAnnotation(RoutePath.class);
            String parentPath = routePathOfType == null ? "" : routePathOfType.value();
            Method[] methods = routeClass.getDeclaredMethods();
            for (Method method : methods) {
                RoutePath routePathOfMethod = method.getDeclaredAnnotation(RoutePath.class);
                if (routePathOfMethod == null) {
                    continue;
                }
                if (returnType != null && !returnType.equals(method.getReturnType())) {
                    continue;
                }
                String childPath = routePathOfMethod.value();
                String fullPath = parentPath + childPath;
                endpoints.add(new HttpRouteEndpoint(fullPath, routePathOfMethod.method(), routeClass, method, route, interceptors));
            }
        }
        return endpoints;
    }

    private boolean isHttpContextValid(HttpContext httpContext) {
        return !httpContext.isRedirected() && !httpContext.isInterrupted() && !httpContext.isEnded();
    }

}
