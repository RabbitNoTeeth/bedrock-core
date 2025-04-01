package com.gitee.rabbitnoteeth.bedrock.core.server.udp;

import com.gitee.rabbitnoteeth.bedrock.core.server.base.AbstractServer;
import com.gitee.rabbitnoteeth.bedrock.core.server.base.handler.ServerDeploymentHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.udp.handler.UdpServerHandler;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramPacket;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class UdpServer extends AbstractServer {

    static final Map<String, UdpServer> SERVER_MAP = new ConcurrentHashMap<>();
    final DatagramSocketOptions options;
    final UdpServerHandler handler;

    UdpServer(String host,
              int port,
              Vertx vertx,
              DeploymentOptions deploymentOptions,
              DatagramSocketOptions options,
              UdpServerHandler handler,
              ServerDeploymentHandler deploymentHandler) {
        super(host, port, vertx, deploymentOptions, deploymentHandler);
        this.options = options;
        this.handler = handler;
        SERVER_MAP.put(this.id, this);
    }

    @Override
    public String name() {
        return "UDP";
    }

    @Override
    public String verticleReference() {
        return "com.gitee.rabbitnoteeth.bedrock.core.server.udp.UdpServerVerticle";
    }

}
