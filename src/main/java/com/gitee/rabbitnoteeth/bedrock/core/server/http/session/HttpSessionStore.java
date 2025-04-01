package com.gitee.rabbitnoteeth.bedrock.core.server.http.session;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;

public interface HttpSessionStore {

    String createSessionId();

    long getSessionTimeout();

    String getSessionId(HttpContext context);

    void writeSessionId(String sessionId, HttpContext context);

    HttpSession getSession(String sessionId, HttpContext context);

    void flushSession(HttpSession httpSession, HttpContext context);

    void removeSession(HttpSession httpSession);

}
