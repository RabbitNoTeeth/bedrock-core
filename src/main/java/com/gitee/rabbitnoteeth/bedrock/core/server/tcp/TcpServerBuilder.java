package com.gitee.rabbitnoteeth.bedrock.core.server.tcp;

import com.gitee.rabbitnoteeth.bedrock.core.server.base.AbstractServerBuilder;
import com.gitee.rabbitnoteeth.bedrock.core.server.tcp.handler.*;
import com.gitee.rabbitnoteeth.bedrock.util.StringUtils;
import com.gitee.rabbitnoteeth.bedrock.core.BedrockCore;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.net.NetServerOptions;

public final class TcpServerBuilder extends AbstractServerBuilder<TcpServerBuilder> {

    private NetServerOptions options;
    private TcpServerHandler handler;

    public TcpServerBuilder(int port) {
        super(port);
    }

    public TcpServerBuilder(String host, int port) {
        super(host, port);
    }

    public TcpServer build() {
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
            this.options = new NetServerOptions();
            if (StringUtils.isNotBlank(this.host)) {
                this.options.setHost(host);
            }
            this.options.setPort(port);
        }
        return new TcpServer(
            this.host,
            this.port,
            this.vertx,
            this.deploymentOptions,
            this.options,
            this.handler,
            this.deploymentHandler
        );
    }

    public NetServerOptions getOptions() {
        return options;
    }

    public TcpServerBuilder setOptions(NetServerOptions options) {
        this.options = options;
        return this;
    }

    public TcpServerHandler getHandler() {
        return handler;
    }

    public TcpServerBuilder setHandler(TcpServerHandler handler) {
        this.handler = handler;
        return this;
    }

}
