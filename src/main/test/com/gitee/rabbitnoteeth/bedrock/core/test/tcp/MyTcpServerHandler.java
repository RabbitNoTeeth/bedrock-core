package com.gitee.rabbitnoteeth.bedrock.core.test.tcp;

import com.gitee.rabbitnoteeth.bedrock.core.server.tcp.handler.TcpServerHandler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTcpServerHandler implements TcpServerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServerHandler.class);

    @Override
    public void onConnect(NetSocket socket) {
        LOGGER.info("connect established");
    }

    @Override
    public void onDisconnect(NetSocket socket) {
        LOGGER.info("connect disestablished");
    }

    @Override
    public void onError(Throwable e, NetSocket socket) {
        LOGGER.error("error", e);
    }

    @Override
    public void onData(Buffer buffer, NetSocket socket) {
        LOGGER.error("receive data, {}", buffer.toString());
    }
}
