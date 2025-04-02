package com.github.rabbitnoteeth.bedrock.core.server.http.entity;

public enum HttpMethod {
    OPTIONS("OPTIONS"),
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    TRACE("TRACE"),
    CONNECT("CONNECT"),
    PATCH("PATCH"),
    PROPFIND("PROPFIND"),
    PROPPATCH("PROPPATCH"),
    MKCOL("MKCOL"),
    COPY("COPY"),
    MOVE("MOVE"),
    LOCK("LOCK"),
    UNLOCK("UNLOCK"),
    MKCALENDAR("MKCALENDAR"),
    VERSION_CONTROL("VERSION_CONTROL"),
    REPORT("REPORT"),
    CHECKOUT("CHECKOUT"),
    CHECKIN("CHECKIN"),
    UNCHECKOUT("UNCHECKOUT"),
    MKWORKSPACE("MKWORKSPACE"),
    UPDATE("UPDATE"),
    LABEL("LABEL"),
    MERGE("MERGE"),
    BASELINE_CONTROL("BASELINE_CONTROL"),
    MKACTIVITY("MKACTIVITY"),
    ORDERPATCH("ORDERPATCH"),
    ACL("ACL"),
    SEARCH("SEARCH"),
    ANY("ANY");

    private final String name;

    HttpMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
