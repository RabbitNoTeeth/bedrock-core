package com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.impl;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.constant.HttpConstants;
import com.gitee.rabbitnoteeth.bedrock.util.StringUtils;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class CorsHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorsHandler.class);

    private final boolean allowCors;
    private final String corsExposeHeaders;

    public CorsHandler(boolean allowCors, String corsExposeHeaders) {
        this.allowCors = allowCors;
        this.corsExposeHeaders = corsExposeHeaders;
    }

    @Override
    public void handle(RoutingContext ctx) {
        // generate request id
        String requestId = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        ctx.put(HttpConstants.REQUEST_ID, requestId);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> http request[{}] arrive CorsHandler", requestId);
        }

        HttpServerRequest request = ctx.request();
        HttpServerResponse response = ctx.response();
        response.setChunked(true);
        if (!allowCors) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("cors is not allowable");
                LOGGER.debug("<<< http request[{}] leave CorsHandler", requestId);
            }
            ctx.next();
            return;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("cors is allowable, write response headers of cors");
        }
        String origin = request.getHeader("Origin");
        if (origin != null) {
            response.putHeader("Access-Control-Allow-Origin", origin);
        }
        String requestHeaders = request.getHeader("Access-Control-Request-Headers");
        if (requestHeaders != null) {
            response.putHeader("Access-Control-Allow-Headers", requestHeaders);
        }
        if (StringUtils.isNotBlank(corsExposeHeaders)) {
            response.putHeader("Access-Control-Expose-Headers", corsExposeHeaders);
        }
        response
            .putHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE")
            .putHeader("Access-Control-Allow-Credentials", "true");
        if (HttpMethod.OPTIONS.equals(request.method())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("<<< http request[{}] leave CorsHandler", requestId);
            }
            ctx.end();
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("<<< http request[{}] leave CorsHandler", requestId);
            }
            ctx.next();
        }
    }

}
