package com.gitee.rabbitnoteeth.bedrock.core.server.http.session;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.sstore.AbstractSession;

public class HttpSession extends AbstractSession {

    private final HttpSessionStore httpSessionStore;
    private final HttpContext httpContext;

    private HttpSession(HttpSessionStore httpSessionStore, HttpContext httpContext) {
        super();
        this.httpSessionStore = httpSessionStore;
        this.httpContext = httpContext;
    }

    public static HttpSession create(String id, long timeout, HttpSessionStore httpSessionStore, HttpContext httpContext) {
        HttpSession httpSession = new HttpSession(httpSessionStore, httpContext);
        httpSession.setId(id);
        httpSession.setTimeout(timeout);
        httpSession.setData(new JsonObject());
        httpSession.setVersion(1);
        httpSessionStore.flushSession(httpSession, httpContext);
        return httpSession;
    }

    public static HttpSession fromJsonString(String jsonStr, HttpSessionStore httpSessionStore, HttpContext httpContext) {
        JsonObject json = new JsonObject(jsonStr);
        HttpSession httpSession = new HttpSession(httpSessionStore, httpContext);
        httpSession.setId(json.getString("id"));
        httpSession.setTimeout(json.getLong("timeout"));
        httpSession.setData(json.getJsonObject("data"));
        httpSession.setVersion(json.getInteger("version"));
        return httpSession;
    }

    public String toJsonString() {
        JsonObject json = new JsonObject();
        json.put("id", this.id());
        json.put("timeout", this.timeout());
        json.put("data", this.data());
        json.put("version", this.version());
        return json.encode();
    }

    @Override
    public HttpSession put(String key, Object obj) {
        super.put(key, obj);
        this.incrementVersion();
        return this;
    }

    @Override
    public HttpSession putIfAbsent(String key, Object obj) {
        super.putIfAbsent(key, obj);
        this.incrementVersion();
        return this;
    }

    @Override
    public <T> T remove(String key) {
        T obj = super.remove(key);
        this.incrementVersion();
        return obj;
    }

    @Override
    public void incrementVersion() {
        super.incrementVersion();
        this.httpSessionStore.flushSession(this, this.httpContext);
    }

}
