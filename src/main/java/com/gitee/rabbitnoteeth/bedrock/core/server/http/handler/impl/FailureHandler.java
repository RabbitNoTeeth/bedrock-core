package com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.impl;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.constant.HttpConstants;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.HttpErrorHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailureHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FailureHandler.class);

    private final HttpErrorHandler errorHandler;

    public FailureHandler(HttpErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void handle(RoutingContext context) {
        String requestId = context.get(HttpConstants.REQUEST_ID);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> http request[{}] arrive FailureHandler", requestId);
        }
        HttpServerResponse response = context.response();
        response.putHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            // 处理404
            int statusCode = context.statusCode();
            if (statusCode == 404) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("<<< http request[{}] leave FailureHandler", requestId);
                }
                response
                    .setStatusCode(404)
                    .end("Resource not found");
                return;
            }
            // 处理异常
            Throwable err = context.failure();
            if (!response.closed()) {
                errorHandler.onError(new HttpContext(context), err);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("<<< http request[{}] leave FailureHandler", requestId);
                }
                context.end();
            }
        } catch (Exception e) {
            if (!response.closed()) {
                try {
                    errorHandler.onError(new HttpContext(context), e);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("<<< http request[{}] leave FailureHandler", requestId);
                    }
                    context.end();
                } catch (Exception ex) {
                    LOGGER.error("there was some error when errorHandler execute", ex);
                    response.setStatusCode(500);
                    context.end();
                }
            }
        }
    }

}
