package com.github.rabbitnoteeth.bedrock.core.server.udp.handler;

import io.vertx.core.datagram.DatagramPacket;
import io.vertx.core.datagram.DatagramSocket;

public interface UdpServerHandler {

    void onConnect(DatagramSocket socket);

    void onDisconnect(DatagramSocket socket);

    void onError(Throwable e, DatagramSocket socket);

    void onData(DatagramPacket packet, DatagramSocket socket);

}
