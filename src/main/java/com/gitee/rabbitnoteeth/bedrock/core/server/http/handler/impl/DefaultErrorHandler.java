package com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.impl;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.HttpErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultErrorHandler implements HttpErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultErrorHandler.class);

    @Override
    public void onError(HttpContext context, Throwable err) throws Exception{
        String requestId = context.getRequestId();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> http request[{}] arrive DefaultErrorHandler", requestId);
        }
        LOGGER.error("some error occurred through the request", err);
        context.putHeader("Content-Type", "application/json;charset=UTF-8");
        context.setStatusCode(500);
        context.interrupt(err.getMessage());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("<<< http request[{}] leave DefaultErrorHandler", requestId);
        }
    }
}
