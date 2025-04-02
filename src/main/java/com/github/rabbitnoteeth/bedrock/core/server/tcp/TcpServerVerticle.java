package com.github.rabbitnoteeth.bedrock.core.server.tcp;

import com.github.rabbitnoteeth.bedrock.core.server.tcp.handler.TcpServerHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

public final class TcpServerVerticle extends AbstractVerticle {

    private NetServer server;

    @Override
    public void start(Promise<Void> promise) throws Exception {
        try {
            JsonObject config = config();
            String ID = config.getString("id");
            TcpServer tcpServer = TcpServer.SERVER_MAP.get(ID);
            if (tcpServer == null) {
                promise.fail(new IllegalArgumentException(String.format("Could not find TcpServer instance with id '%s'", ID)));
            } else {
                NetServerOptions options = tcpServer.options;
                server = vertx.createNetServer(options);
                server.connectHandler(socket -> {
                    TcpServerHandler handler = tcpServer.handler;
                    if (handler != null) {
                        socket.drainHandler(v -> handler.onConnect(socket));
                        socket.closeHandler(v -> handler.onDisconnect(socket));
                        socket.exceptionHandler(e -> handler.onError(e, socket));
                        socket.handler(buffer -> handler.onData(buffer, socket));
                    }
                });
                server.listen(res -> {
                    if (!res.succeeded()) {
                        promise.fail(res.cause());
                    } else {
                        promise.complete();
                    }
                });
            }
        } catch (Exception e) {
            promise.fail(e);
        }
    }

    @Override
    public void stop() throws Exception {
        server.close();
    }

}
