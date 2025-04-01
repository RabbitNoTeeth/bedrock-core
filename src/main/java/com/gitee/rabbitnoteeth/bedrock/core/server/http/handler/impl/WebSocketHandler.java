package com.gitee.rabbitnoteeth.bedrock.core.server.http.handler.impl;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.route.HttpWebSocketRoute;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler implements Handler<ServerWebSocket> {

    private final Map<String, HttpWebSocketRoute> endPointMap = new ConcurrentHashMap<>();

    public WebSocketHandler(List<HttpWebSocketRoute> endPoints) {
        endPoints.forEach(ep -> endPointMap.put(ep.path(), ep));
    }

    @Override
    public void handle(ServerWebSocket socket) {
        String path = socket.path();
        HttpWebSocketRoute endPoint = endPointMap.get(path);
        if (endPoint == null) {
            socket.reject(404);
        } else {
            socket.writePing(Buffer.buffer("ping"));
            socket.pongHandler(event -> endPoint.onOpen(socket));
            socket.textMessageHandler(message -> endPoint.onMessage(socket, message));
            socket.exceptionHandler(e -> endPoint.onException(socket, e));
            socket.closeHandler(v -> endPoint.onClose(socket));
        }
    }

}
