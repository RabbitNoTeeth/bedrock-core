package com.gitee.rabbitnoteeth.bedrock.core.server.http;

import com.gitee.rabbitnoteeth.bedrock.core.server.base.AbstractServerBuilder;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.impl.DefaultErrorHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.HttpErrorHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.HttpResponseHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.HttpRequestHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.route.*;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.session.HttpSessionStore;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.template.TemplateEngine;
import com.gitee.rabbitnoteeth.bedrock.util.StringUtils;
import com.gitee.rabbitnoteeth.bedrock.core.BedrockCore;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.NetServerOptions;

import java.util.ArrayList;
import java.util.List;

public final class HttpServerBuilder extends AbstractServerBuilder<HttpServerBuilder> {

    private static final String DEFAULT_STATIC_DICTIONARY = "static";
    private static final String DEFAULT_STATIC_PATH_PREFIX = "/static";
    private static final String DEFAULT_INDEX_PAGE = "index.html";
    private static final TemplateEngine DEFAULT_TEMPLATE_ENGINE = TemplateEngine.THYMELEAF;
    private static final String DEFAULT_TEMPLATE_DICTIONARY = "template";
    private static final String DEFAULT_TEMPLATE_FILE_TYPE = "html";
    private static final String DEFAULT_TEMPLATE_CONTENT_TYPE = "text/html;charset=utf-8";
    private static final String DEFAULT_TEMPLATE_PATH_PREFIX = "/page";
    private static final String DEFAULT_REST_API_PATH_PREFIX = "/api";
    private static final String DEFAULT_SOCKJS_PATH_PREFIX = "/sockjs";
    private static final Boolean DEFAULT_ALLOW_CORS = false;
    private HttpServerOptions options;
    private String staticResourceDir;
    private String staticResourcePathPrefix;
    private String indexPage;
    private TemplateEngine templateEngine;
    private String templateResourceDir;
    private String templateFileType;
    private String templateContentType;
    private String templateResourcePathPrefix;
    private String restApiPathPrefix;
    private String sockjsPathPrefix;
    private Boolean allowCors;
    private String corsExposeHeaders;
    private HttpSessionStore httpSessionStore;
    private List<HttpWebSocketRoute> websocketRoutes;
    private List<HttpSockjsRoute> sockjsRoutes;
    private List<HttpTemplateRoute> templateRoutes;
    private List<HttpRestRoute> restRoutes;
    private List<HttpRouteInterceptor> restRouteInterceptors;
    private List<HttpRouteInterceptor> templateRouteInterceptors;
    private List<HttpRequestHandler> requestHandlers;
    private List<HttpResponseHandler> responseHandlers;
    private HttpErrorHandler errorHandler;

    public HttpServerBuilder(int port) {
        super(port);
    }

    public HttpServerBuilder(String host, int port) {
        super(host, port);
    }

    public HttpServer build() {
        if (this.port <= 0) {
            throw new IllegalArgumentException("invalid port");
        }
        if (this.vertx == null) {
            this.vertx = BedrockCore.getVertx();
        }
        if (this.deploymentOptions == null) {
            this.deploymentOptions = new DeploymentOptions();
        }
        if (this.options == null) {
            this.options = new HttpServerOptions()
                .setIdleTimeout(5)
                .setMaxFormAttributeSize(-1);
            if (StringUtils.isNotBlank(this.host)) {
                this.options.setHost(host);
            }
            this.options.setPort(port);
        }
        if (StringUtils.isBlank(this.staticResourceDir)) {
            this.staticResourceDir = DEFAULT_STATIC_DICTIONARY;
        }
        if (this.staticResourcePathPrefix == null) {
            this.staticResourcePathPrefix = DEFAULT_STATIC_PATH_PREFIX;
        }
        if (StringUtils.isBlank(this.indexPage)) {
            this.indexPage = DEFAULT_INDEX_PAGE;
        }
        if (this.templateEngine == null) {
            this.templateEngine = DEFAULT_TEMPLATE_ENGINE;
        }
        if (StringUtils.isBlank(this.templateResourceDir)) {
            this.templateResourceDir = DEFAULT_TEMPLATE_DICTIONARY;
        }
        if (StringUtils.isBlank(this.templateFileType)) {
            this.templateFileType = DEFAULT_TEMPLATE_FILE_TYPE;
        }
        if (StringUtils.isBlank(this.templateContentType)) {
            this.templateContentType = DEFAULT_TEMPLATE_CONTENT_TYPE;
        }
        if (this.templateResourcePathPrefix == null) {
            this.templateResourcePathPrefix = DEFAULT_TEMPLATE_PATH_PREFIX;
        }
        if (this.restApiPathPrefix == null) {
            this.restApiPathPrefix = DEFAULT_REST_API_PATH_PREFIX;
        }
        if (this.allowCors == null) {
            this.allowCors = DEFAULT_ALLOW_CORS;
        }
        if (this.websocketRoutes == null) {
            this.websocketRoutes = new ArrayList<>();
        }
        if (this.sockjsRoutes == null) {
            this.sockjsRoutes = new ArrayList<>();
        }
        if (this.requestHandlers == null) {
            this.requestHandlers = new ArrayList<>();
        }
        if (this.responseHandlers == null) {
            this.responseHandlers = new ArrayList<>();
        }
        if (this.sockjsPathPrefix == null) {
            this.sockjsPathPrefix = DEFAULT_SOCKJS_PATH_PREFIX;
        }
        if (this.templateRoutes == null) {
            this.templateRoutes = new ArrayList<>();
        }
        if (this.restRoutes == null) {
            this.restRoutes = new ArrayList<>();
        }
        if (this.restRouteInterceptors == null) {
            this.restRouteInterceptors = new ArrayList<>();
        }
        if (this.templateRouteInterceptors == null) {
            this.templateRouteInterceptors = new ArrayList<>();
        }
        if (this.errorHandler == null) {
            this.errorHandler = new DefaultErrorHandler();
        }
        return new HttpServer(
            this.host,
            this.port,
            this.vertx,
            this.deploymentOptions,
            this.options,
            this.deploymentHandler,
            this.staticResourceDir,
            this.staticResourcePathPrefix,
            this.indexPage,
            this.templateEngine,
            this.templateResourceDir,
            this.templateFileType,
            this.templateContentType,
            this.templateResourcePathPrefix,
            this.restApiPathPrefix,
            this.sockjsPathPrefix,
            this.allowCors,
            this.corsExposeHeaders,
            this.httpSessionStore,
            this.websocketRoutes,
            this.sockjsRoutes,
            this.templateRoutes,
            this.restRoutes,
            this.restRouteInterceptors,
            this.templateRouteInterceptors,
            this.requestHandlers,
            this.responseHandlers,
            this.errorHandler);
    }

    public NetServerOptions getOptions() {
        return options;
    }

    public HttpServerBuilder setOptions(HttpServerOptions options) {
        this.options = options;
        return this;
    }

    public String getStaticResourceDir() {
        return staticResourceDir;
    }

    public HttpServerBuilder setStaticResourceDir(String staticResourceDir) {
        this.staticResourceDir = staticResourceDir;
        return this;
    }

    public String getStaticResourcePathPrefix() {
        return staticResourcePathPrefix;
    }

    public HttpServerBuilder setStaticResourcePathPrefix(String staticResourcePathPrefix) {
        this.staticResourcePathPrefix = staticResourcePathPrefix;
        return this;
    }

    public String getIndexPage() {
        return indexPage;
    }

    public HttpServerBuilder setIndexPage(String indexPage) {
        this.indexPage = indexPage;
        return this;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public HttpServerBuilder setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        return this;
    }

    public String getTemplateResourceDir() {
        return templateResourceDir;
    }

    public HttpServerBuilder setTemplateResourceDir(String templateResourceDir) {
        this.templateResourceDir = templateResourceDir;
        return this;
    }

    public String getTemplateFileType() {
        return templateFileType;
    }

    public HttpServerBuilder setTemplateFileType(String templateFileType) {
        this.templateFileType = templateFileType;
        return this;
    }

    public String getTemplateContentType() {
        return templateContentType;
    }

    public HttpServerBuilder setTemplateContentType(String templateContentType) {
        this.templateContentType = templateContentType;
        return this;
    }

    public String getTemplateResourcePathPrefix() {
        return templateResourcePathPrefix;
    }

    public HttpServerBuilder setTemplateResourcePathPrefix(String templateResourcePathPrefix) {
        this.templateResourcePathPrefix = templateResourcePathPrefix;
        return this;
    }

    public String getRestApiPathPrefix() {
        return restApiPathPrefix;
    }

    public HttpServerBuilder setRestApiPathPrefix(String restApiPathPrefix) {
        this.restApiPathPrefix = restApiPathPrefix;
        return this;
    }

    public List<HttpWebSocketRoute> getWebsocketRoutes() {
        return websocketRoutes;
    }

    public HttpServerBuilder setWebsocketRoutes(List<HttpWebSocketRoute> websocketRoutes) {
        this.websocketRoutes = websocketRoutes;
        return this;
    }

    public List<HttpRequestHandler> getRequestHandlers() {
        return requestHandlers;
    }

    public HttpServerBuilder setRequestHandlers(List<HttpRequestHandler> requestHandlers) {
        this.requestHandlers = requestHandlers;
        return this;
    }

    public List<HttpSockjsRoute> getSockjsRoutes() {
        return sockjsRoutes;
    }

    public HttpServerBuilder setSockjsRoutes(List<HttpSockjsRoute> sockjsRoutes) {
        this.sockjsRoutes = sockjsRoutes;
        return this;
    }

    public String getSockjsPathPrefix() {
        return sockjsPathPrefix;
    }

    public HttpServerBuilder setSockjsPathPrefix(String sockjsPathPrefix) {
        this.sockjsPathPrefix = sockjsPathPrefix;
        return this;
    }

    public List<HttpTemplateRoute> getTemplateRoutes() {
        return templateRoutes;
    }

    public HttpServerBuilder setTemplateRoutes(List<HttpTemplateRoute> templateRoutes) {
        this.templateRoutes = templateRoutes;
        return this;
    }

    public List<HttpResponseHandler> getResponseHandlers() {
        return responseHandlers;
    }

    public HttpServerBuilder setResponseHandlers(List<HttpResponseHandler> responseHandlers) {
        this.responseHandlers = responseHandlers;
        return this;
    }

    public List<HttpRestRoute> getRestRoutes() {
        return restRoutes;
    }

    public HttpServerBuilder setRestRoutes(List<HttpRestRoute> restRoutes) {
        this.restRoutes = restRoutes;
        return this;
    }

    public HttpErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public HttpServerBuilder setErrorHandler(HttpErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public HttpSessionStore getSessionStore() {
        return httpSessionStore;
    }

    public HttpServerBuilder setSessionStore(HttpSessionStore httpSessionStore) {
        this.httpSessionStore = httpSessionStore;
        return this;
    }

    public Boolean getAllowCors() {
        return allowCors;
    }

    public HttpServerBuilder setAllowCors(Boolean allowCors) {
        this.allowCors = allowCors;
        return this;
    }

    public String getCorsExposeHeaders() {
        return corsExposeHeaders;
    }

    public HttpServerBuilder setCorsExposeHeaders(String corsExposeHeaders) {
        this.corsExposeHeaders = corsExposeHeaders;
        return this;
    }

    public List<HttpRouteInterceptor> getRestRouteInterceptors() {
        return restRouteInterceptors;
    }

    public HttpServerBuilder setRestRouteInterceptors(List<HttpRouteInterceptor> restRouteInterceptors) {
        this.restRouteInterceptors = restRouteInterceptors;
        return this;
    }

    public List<HttpRouteInterceptor> getTemplateRouteInterceptors() {
        return templateRouteInterceptors;
    }

    public HttpServerBuilder setTemplateRouteInterceptors(List<HttpRouteInterceptor> templateRouteInterceptors) {
        this.templateRouteInterceptors = templateRouteInterceptors;
        return this;
    }
}
