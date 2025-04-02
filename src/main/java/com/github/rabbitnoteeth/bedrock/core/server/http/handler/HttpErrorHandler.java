package com.github.rabbitnoteeth.bedrock.core.server.http.handler;

import com.github.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;

public interface HttpErrorHandler {

    void onError(HttpContext context, Throwable err) throws Exception;

}
