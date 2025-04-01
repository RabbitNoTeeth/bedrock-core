package com.gitee.rabbitnoteeth.bedrock.core.server.http.handler;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;

public interface HttpRequestHandler {

    default boolean async() {
        return false;
    }

    int order();

    void execute(HttpContext context) throws Exception;
}
