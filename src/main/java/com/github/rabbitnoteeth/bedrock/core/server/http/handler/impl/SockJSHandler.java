package com.github.rabbitnoteeth.bedrock.core.server.http.handler.impl;

import com.github.rabbitnoteeth.bedrock.core.server.http.route.HttpSockjsRoute;
import io.vertx.core.Handler;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

import java.nio.charset.StandardCharsets;

public class SockJSHandler implements Handler<SockJSSocket> {

    private final HttpSockjsRoute endPoint;

    public SockJSHandler(HttpSockjsRoute endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public void handle(SockJSSocket socket) {
        endPoint.onOpen(socket);
        socket.handler(message -> endPoint.onMessage(socket, message.toString(StandardCharsets.UTF_8)));
        socket.drainHandler(v -> endPoint.onDrain(socket));
        socket.exceptionHandler(e -> endPoint.onException(socket, e));
        socket.closeHandler(v -> endPoint.onClose(socket));
        socket.endHandler(v -> endPoint.onEnd(socket));
    }

}
