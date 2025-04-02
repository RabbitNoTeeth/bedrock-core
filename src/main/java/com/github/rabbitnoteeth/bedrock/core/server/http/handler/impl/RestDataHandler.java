package com.github.rabbitnoteeth.bedrock.core.server.http.handler.impl;

import com.github.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.github.rabbitnoteeth.bedrock.util.JsonUtils;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestDataHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestDataHandler.class);

    @Override
    public void handle(RoutingContext context) {
        HttpContext wrappedContext = new HttpContext(context);
        Object data = wrappedContext.getResponseData();
        wrappedContext.putHeader("Content-Type", "application/json;charset=UTF-8");
        if (data != null) {
            context.response().write(JsonUtils.encode(data));
        }
        context.next();
    }

}
