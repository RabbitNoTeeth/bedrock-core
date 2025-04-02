package com.github.rabbitnoteeth.bedrock.core.server.tcp.handler;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public interface TcpServerHandler {

    void onConnect(NetSocket socket);

    void onDisconnect(NetSocket socket);

    void onError(Throwable e, NetSocket socket);

    void onData(Buffer buffer, NetSocket socket);

}
