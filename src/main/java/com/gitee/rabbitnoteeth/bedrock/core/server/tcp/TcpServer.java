package com.gitee.rabbitnoteeth.bedrock.core.server.tcp;

import com.gitee.rabbitnoteeth.bedrock.core.server.base.AbstractServer;
import com.gitee.rabbitnoteeth.bedrock.core.server.base.handler.ServerDeploymentHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.tcp.handler.*;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
        return "com.gitee.rabbitnoteeth.bedrock.core.server.tcp.TcpServerVerticle";
    }

}
