package com.github.rabbitnoteeth.bedrock.core.server.http.context.request;

import io.vertx.core.MultiMap;

public class HttpRequestHeaders extends HttpRequestData {

    public HttpRequestHeaders(MultiMap map) {
        super(map);
    }

    @Override
    public String toString() {
        return isEmpty() ? "" : map.toString().replaceAll(System.lineSeparator(), ";");
    }

}
