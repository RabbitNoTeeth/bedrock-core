package com.gitee.rabbitnoteeth.bedrock.core.server.http.context.request;

import io.vertx.core.MultiMap;

public class HttpRequestParams extends HttpRequestData {

    public HttpRequestParams(MultiMap map) {
        super(map);
    }

    @Override
    public String toString() {
        return isEmpty() ? "" : map.toString().replaceAll(System.lineSeparator(), ";");
    }
}
