package com.github.rabbitnoteeth.bedrock.core.server.tcp;

import com.github.rabbitnoteeth.bedrock.core.server.base.AbstractServer;
import com.github.rabbitnoteeth.bedrock.core.server.base.handler.ServerDeploymentHandler;
import com.github.rabbitnoteeth.bedrock.core.server.tcp.handler.TcpServerHandler;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServerOptions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TcpServer extends AbstractServer {

    static final Map<String, TcpServer> SERVER_MAP = new ConcurrentHashMap<>();
    final NetServerOptions options;
    final TcpServerHandler handler;

    TcpServer(String host,
              int port,
              Vertx vertx,
              DeploymentOptions deploymentOptions,
              NetServerOptions options,
              TcpServerHandler handler,
              ServerDeploymentHandler deploymentHandler) {
        super(host, port, vertx, deploymentOptions, deploymentHandler);
        this.options = options;
        this.handler = handler;
        SERVER_MAP.put(this.id, this);
    }

    @Override
    public String name() {
        return "TCP";
    }

    @Override
    public String verticleReference() {
        return "com.github.rabbitnoteeth.bedrock.core.server.tcp.TcpServerVerticle";
    }

}
