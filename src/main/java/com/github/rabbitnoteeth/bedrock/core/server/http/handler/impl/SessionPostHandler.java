package com.github.rabbitnoteeth.bedrock.core.server.http.handler.impl;

import com.github.rabbitnoteeth.bedrock.core.server.http.constant.HttpConstants;
import com.github.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.github.rabbitnoteeth.bedrock.core.server.http.session.HttpSession;
import com.github.rabbitnoteeth.bedrock.core.server.http.session.HttpSessionStore;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionPostHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionPostHandler.class);

    private final HttpSessionStore httpSessionStore;

    public SessionPostHandler(HttpSessionStore httpSessionStore) {
        this.httpSessionStore = httpSessionStore;
    }

    @Override
    public void handle(RoutingContext context) {
        String requestId = context.get(HttpConstants.REQUEST_ID);
        HttpContext wrappedContext = new HttpContext(context);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> http request[{}] arrive SessionPostHandler", requestId);
        }
        if (httpSessionStore != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sessionStore configured");
            }
            io.vertx.ext.web.Session session = context.session();
            if (session instanceof HttpSession) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("flush session");
                }
                httpSessionStore.flushSession((HttpSession) session, wrappedContext);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("there is no sessionStore configured");
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("<<< http request[{}] leave SessionPostHandler", requestId);
        }
        context.next();
    }

}
