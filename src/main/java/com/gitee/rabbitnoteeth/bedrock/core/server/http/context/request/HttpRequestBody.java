package com.gitee.rabbitnoteeth.bedrock.core.server.http.context.request;

import com.gitee.rabbitnoteeth.bedrock.util.JsonUtils;
import com.google.gson.JsonElement;

import java.nio.charset.StandardCharsets;

public class HttpRequestBody {

    private final io.vertx.ext.web.RequestBody body;

    public HttpRequestBody(io.vertx.ext.web.RequestBody body) {
        this.body = body;
    }

    public boolean isEmpty() {
        return body.isEmpty();
    }

    public int length() {
        return body.length();
    }

    public String asString() {
        return body.asString();
    }

    public String asString(String encoding) {
        return body.asString(encoding);
    }

    public JsonElement asJson() {
        return JsonUtils.decode(body.asString(StandardCharsets.UTF_8.toString()));
    }

    @Override
    public String toString() {
        return isEmpty() ? "" : body.asString(StandardCharsets.UTF_8.toString());
    }
}
