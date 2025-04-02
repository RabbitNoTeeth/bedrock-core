package com.github.rabbitnoteeth.bedrock.core.test.udp;

import com.github.rabbitnoteeth.bedrock.core.server.tcp.handler.TcpServerHandler;
import com.github.rabbitnoteeth.bedrock.core.server.udp.handler.UdpServerHandler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramPacket;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyUdpServerHandler implements UdpServerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServerHandler.class);

    @Override
    public void onConnect(DatagramSocket socket) {
        LOGGER.info("connect established");
    }

    @Override
    public void onDisconnect(DatagramSocket socket) {
        LOGGER.info("connect disestablished");
    }

    @Override
    public void onError(Throwable e, DatagramSocket socket) {
        LOGGER.error("error", e);
    }

    @Override
    public void onData(DatagramPacket packet, DatagramSocket socket) {
        LOGGER.error("receive data, {}", packet.toString());
    }
}
