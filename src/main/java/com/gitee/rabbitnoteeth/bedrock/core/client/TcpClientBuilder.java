package com.gitee.rabbitnoteeth.bedrock.core.client;

import com.gitee.rabbitnoteeth.bedrock.core.BedrockCore;
import com.gitee.rabbitnoteeth.bedrock.util.StringUtils;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClientOptions;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class TcpClientBuilder {

    private final String host;
    private final int port;
    private Vertx vertx = BedrockCore.getVertx();
    private NetClientOptions options = new NetClientOptions();
    private Consumer<TcpClient> connectHandler;
    private Consumer<TcpClient> closeHandler;
    private BiConsumer<Throwable, TcpClient> connectErrorHandler;
    private BiConsumer<Throwable, TcpClient> errorHandler;
    private BiConsumer<Buffer, TcpClient> dataHandler;

    private TcpClientBuilder(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static TcpClientBuilder create(String host, int port) {
        return new TcpClientBuilder(host, port);
    }

    public TcpClient build() {
        if (StringUtils.isBlank(this.host)) {
            throw new IllegalArgumentException("invalid host");
        }
        if (this.port <= 0) {
            throw new IllegalArgumentException("invalid port");
        }
        return new TcpClient(
            this.host,
            this.port,
            this.vertx,
            this.options,
            this.connectHandler,
            this.closeHandler,
            this.connectErrorHandler,
            this.errorHandler,
            this.dataHandler
        );
    }

    public TcpClientBuilder setVertx(Vertx vertx) {
        this.vertx = vertx;
        return this;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public TcpClientBuilder setOptions(NetClientOptions options) {
        this.options = options;
        return this;
    }

    public TcpClientBuilder setConnectHandler(Consumer<TcpClient> connectHandler) {
        this.connectHandler = connectHandler;
        return this;
    }

    public TcpClientBuilder setConnectErrorHandler(BiConsumer<Throwable, TcpClient> connectErrorHandler) {
        this.connectErrorHandler = connectErrorHandler;
        return this;
    }

    public TcpClientBuilder setDataHandler(BiConsumer<Buffer, TcpClient> dataHandler) {
        this.dataHandler = dataHandler;
        return this;
    }

    public TcpClientBuilder setErrorHandler(BiConsumer<Throwable, TcpClient> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public TcpClientBuilder setCloseHandler(Consumer<TcpClient> closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

}
