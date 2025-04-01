package com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.impl;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.constant.HttpConstants;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.session.HttpSession;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.session.HttpSessionStore;
import com.gitee.rabbitnoteeth.bedrock.util.StringUtils;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RoutingContextInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionPreHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionPreHandler.class);

    private final HttpSessionStore httpSessionStore;

    public SessionPreHandler(HttpSessionStore httpSessionStore) {
        this.httpSessionStore = httpSessionStore;
    }

    @Override
    public void handle(RoutingContext context) {
        String requestId = context.get(HttpConstants.REQUEST_ID);
        HttpContext wrappedContext = new HttpContext(context);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> http request[{}] arrive SessionPreHandler", requestId);
        }
        if (httpSessionStore != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sessionStore configured");
            }
            String sessionId = httpSessionStore.getSessionId(wrappedContext);
            if (StringUtils.isBlank(sessionId)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("there is no sessionId in request, create a new one");
                }
                sessionId = httpSessionStore.createSessionId();
                httpSessionStore.writeSessionId(sessionId, wrappedContext);
            }
            HttpSession httpSession = httpSessionStore.getSession(sessionId, wrappedContext);
            if (httpSession == null || httpSession.timeout() <= 0) {
                if (httpSession != null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("session has expired, remove it and create a new one");
                    }
                    httpSessionStore.removeSession(httpSession);
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("there is no session, create a new one");
                    }
                }
                httpSession = HttpSession.create(sessionId, httpSessionStore.getSessionTimeout(), httpSessionStore, wrappedContext);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("create session successfully");
                }
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("set session to the request");
            }
            ((RoutingContextInternal) context).setSession(httpSession);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("there is no sessionStore configured");
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("<<< http request[{}] leave SessionPreHandler", requestId);
        }
        context.next();
    }

}
