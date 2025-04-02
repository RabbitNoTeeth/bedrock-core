package com.github.rabbitnoteeth.bedrock.core.server.udp;

import com.github.rabbitnoteeth.bedrock.core.server.base.AbstractServerBuilder;
import com.github.rabbitnoteeth.bedrock.core.BedrockCore;
import com.github.rabbitnoteeth.bedrock.core.server.udp.handler.UdpServerHandler;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.datagram.DatagramSocketOptions;

public final class UdpServerBuilder extends AbstractServerBuilder<UdpServerBuilder> {

    private DatagramSocketOptions options;
    private UdpServerHandler handler;

    public UdpServerBuilder(int port) {
        super(port);
    }

    public UdpServerBuilder(String host, int port) {
        super(host, port);
    }

    public UdpServer build() {
        if (this.port <= 0) {
            throw new IllegalArgumentException("invalid port");
        }
        if (this.vertx == null) {
            this.vertx = BedrockCore.getVertx();
        }
        if (this.deploymentOptions == null) {
            this.deploymentOptions = new DeploymentOptions();
        }
        if (this.options == null) {
            this.options = new DatagramSocketOptions();
        }
        return new UdpServer(
            this.host,
            this.port,
            this.vertx,
            this.deploymentOptions,
            this.options,
            this.handler,
            this.deploymentHandler
        );
    }

    public DatagramSocketOptions getOptions() {
        return options;
    }

    public UdpServerBuilder setOptions(DatagramSocketOptions options) {
        this.options = options;
        return this;
    }

    public UdpServerHandler getHandler() {
        return handler;
    }

    public UdpServerBuilder setHandler(UdpServerHandler handler) {
        this.handler = handler;
        return this;
    }

}
