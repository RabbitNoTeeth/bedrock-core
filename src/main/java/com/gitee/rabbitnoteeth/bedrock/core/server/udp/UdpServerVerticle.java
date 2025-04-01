package com.gitee.rabbitnoteeth.bedrock.core.server.udp;

import com.gitee.rabbitnoteeth.bedrock.core.server.udp.handler.UdpServerHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.json.JsonObject;

public final class UdpServerVerticle extends AbstractVerticle {

    private DatagramSocket socket;

    @Override
    public void start(Promise<Void> promise) throws Exception {
        try {
            JsonObject config = config();
            String ID = config.getString("id");
            UdpServer udpServer = UdpServer.SERVER_MAP.get(ID);
            if (udpServer == null) {
                promise.fail(new IllegalArgumentException(String.format("Could not find UdpServer instance with id '%s'", ID)));
            } else {
                socket = vertx.createDatagramSocket(udpServer.options);
                socket.listen(udpServer.port(), udpServer.host(), res -> {
                    if (!res.succeeded()) {
                        promise.fail(res.cause());
                    } else {
                        UdpServerHandler handler = udpServer.handler;
                        if (handler != null) {
                            handler.onConnect(socket);
                            socket.endHandler(v -> handler.onDisconnect(socket));
                            socket.exceptionHandler(e -> handler.onError(e, socket));
                            socket.handler(buffer -> handler.onData(buffer, socket));
                        }
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
        socket.close();
    }

}
