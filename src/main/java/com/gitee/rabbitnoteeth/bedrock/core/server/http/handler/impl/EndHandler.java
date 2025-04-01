package com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.impl;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.constant.HttpConstants;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndHandler.class);

    @Override
    public void handle(RoutingContext context) {
        String requestId = context.get(HttpConstants.REQUEST_ID);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> http request[{}] arrive EndHandler", requestId);
            LOGGER.debug("<<< http request[{}] leave EndHandler", requestId);
        }
        context.end();
    }

}
