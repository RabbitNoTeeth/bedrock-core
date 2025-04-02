package com.github.rabbitnoteeth.bedrock.core.server.http.route;

import io.vertx.ext.web.handler.sockjs.SockJSSocket;

public interface HttpSockjsRoute {

    String path();

    void onOpen(SockJSSocket socket);

    void onMessage(SockJSSocket socket, String message);

    void onDrain(SockJSSocket socket);

    void onException(SockJSSocket socket, Throwable e);

    void onClose(SockJSSocket socket);

    void onEnd(SockJSSocket socket);

}
