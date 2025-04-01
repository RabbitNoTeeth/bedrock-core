package com.gitee.rabbitnoteeth.bedrock.core.server.http.route;

import io.vertx.core.http.ServerWebSocket;

public interface HttpWebSocketRoute {

    String path();

    void onOpen(ServerWebSocket socket);

    void onMessage(ServerWebSocket socket, String message);

    void onClose(ServerWebSocket socket);

    void onException(ServerWebSocket socket, Throwable e);

}
