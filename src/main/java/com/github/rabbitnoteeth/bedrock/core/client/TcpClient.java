package com.github.rabbitnoteeth.bedrock.core.client;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class TcpClient {

    private final NetClient client;
    private final String host;
    private final int port;
    private final Consumer<TcpClient> connectHandler;
    private final Consumer<TcpClient> closeHandler;
    private final BiConsumer<Throwable, TcpClient> connectErrorHandler;
    private final BiConsumer<Throwable, TcpClient> errorHandler;
    private final BiConsumer<Buffer, TcpClient> dataHandler;
    private NetSocket socket;

    TcpClient(String host,
                      int port,
                      Vertx vertx,
                      NetClientOptions options,
                      Consumer<TcpClient> connectHandler,
                      Consumer<TcpClient> closeHandler,
                      BiConsumer<Throwable, TcpClient> connectErrorHandler,
                      BiConsumer<Throwable, TcpClient> errorHandler,
                      BiConsumer<Buffer, TcpClient> dataHandler) {
        this.host = host;
        this.port = port;
        this.connectHandler = connectHandler;
        this.closeHandler = closeHandler;
        this.connectErrorHandler = connectErrorHandler;
        this.errorHandler = errorHandler;
        this.dataHandler = dataHandler;
        this.client = vertx.createNetClient(options);
    }

    public void connect() {
        client.connect(port, host, res -> {
            if (!res.succeeded()) {
                if (this.connectErrorHandler != null) {
                    this.connectErrorHandler.accept(res.cause(), this);
                }
            } else {
                socket = res.result();
                if (this.connectHandler != null) {
                    socket.drainHandler(v -> connectHandler.accept(this));
                }
                if (this.errorHandler != null) {
                    socket.exceptionHandler(e -> errorHandler.accept(e, this));
                }
                if (this.closeHandler != null) {
                    socket.closeHandler(v -> closeHandler.accept(this));
                }
                if (this.dataHandler != null) {
                    socket.handler(buffer -> dataHandler.accept(buffer, this));
                }
            }
        });
    }

    public void write(Buffer message) {
        socket.write(message);
    }

    public void close() {
        client.close();
    }

}
