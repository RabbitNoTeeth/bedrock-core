package com.gitee.rabbitnoteeth.bedrock.core.server.http.route.exception;

public class HttpRouteEndpointException extends Exception {

    public HttpRouteEndpointException(String message) {
        super(message);
    }

    public HttpRouteEndpointException(Throwable e) {
        super(e);
    }

    public HttpRouteEndpointException(String message, Throwable e) {
        super(message, e);
    }

}
