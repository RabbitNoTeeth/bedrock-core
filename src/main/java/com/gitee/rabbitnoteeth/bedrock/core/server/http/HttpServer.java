package com.gitee.rabbitnoteeth.bedrock.core.server.http;

import com.gitee.rabbitnoteeth.bedrock.core.server.base.AbstractServer;
import com.gitee.rabbitnoteeth.bedrock.core.server.base.handler.ServerDeploymentHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.HttpErrorHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.HttpResponseHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.HttpRequestHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.route.*;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.session.HttpSessionStore;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.template.TemplateEngine;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class HttpServer extends AbstractServer {

    static final Map<String, HttpServer> SERVER_MAP = new ConcurrentHashMap<>();
    final HttpServerOptions options;
    final String staticResourceDir;
    final String staticResourcePathPrefix;
    final String indexPage;
    final TemplateEngine templateEngine;
    final String templateResourceDir;
    final String templateFileType;
    final String templateContentType;
    final String templateResourcePathPrefix;
    final String restApiPathPrefix;
    final String sockjsPathPrefix;
    final Boolean allowCors;
    final String corsExposeHeaders;
    final HttpSessionStore sessionStore;;
    final List<HttpWebSocketRoute> websocketRoutes;
    final List<HttpSockjsRoute> sockjsRoutes;
    final List<HttpTemplateRoute> templateRoutes;
    final List<HttpRestRoute> restRoutes;
    final List<HttpRouteInterceptor> restRouteInterceptors;
    final List<HttpRouteInterceptor> templateRouteInterceptors;
    final List<HttpRequestHandler> requestHandlers;
    final List<HttpResponseHandler> responseHandlers;
    final HttpErrorHandler errorHandler;
    HttpServer(String host,
               int port,
               Vertx vertx,
               DeploymentOptions deploymentOptions,
               HttpServerOptions options,
               ServerDeploymentHandler deploymentHandler,
               String staticResourceDir,
               String staticResourcePathPrefix,
               String indexPage,
               TemplateEngine templateEngine,
               String templateResourceDir,
               String templateFileType,
               String templateContentType,
               String templateResourcePathPrefix,
               String restApiPathPrefix,
               String sockjsPathPrefix,
               Boolean allowCors,
               String corsExposeHeaders, HttpSessionStore sessionStore,
               List<HttpWebSocketRoute> websocketRoutes,
               List<HttpSockjsRoute> sockjsRoutes,
               List<HttpTemplateRoute> templateRoutes,
               List<HttpRestRoute> restRoutes,
               List<HttpRouteInterceptor> restRouteInterceptors,
               List<HttpRouteInterceptor> templateRouteInterceptors,
               List<HttpRequestHandler> requestHandlers,
               List<HttpResponseHandler> responseHandlers,
               HttpErrorHandler errorHandler) {
        super(host, port, vertx, deploymentOptions, deploymentHandler);
        this.options = options;
        this.staticResourceDir = staticResourceDir;
        this.staticResourcePathPrefix = staticResourcePathPrefix;
        this.indexPage = indexPage;
        this.templateEngine = templateEngine;
        this.templateResourceDir = templateResourceDir;
        this.templateFileType = templateFileType;
        this.templateContentType = templateContentType;
        this.templateResourcePathPrefix = templateResourcePathPrefix;
        this.restApiPathPrefix = restApiPathPrefix;
        this.sockjsPathPrefix = sockjsPathPrefix;
        this.allowCors = allowCors;
        this.corsExposeHeaders = corsExposeHeaders;
        this.sessionStore = sessionStore;
        this.websocketRoutes = websocketRoutes;
        this.restRoutes = restRoutes;
        this.restRouteInterceptors = restRouteInterceptors;
        this.templateRouteInterceptors = templateRouteInterceptors;
        this.requestHandlers = requestHandlers;
        this.sockjsRoutes = sockjsRoutes;
        this.templateRoutes = templateRoutes;
        this.responseHandlers = responseHandlers;
        this.errorHandler = errorHandler;
        SERVER_MAP.put(this.id, this);
    }

    @Override
    public String name() {
        return "HTTP";
    }

    @Override
    public String verticleReference() {
        return "com.gitee.rabbitnoteeth.bedrock.core.server.http.HttpServerVerticle";
    }
}
