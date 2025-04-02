package com.github.rabbitnoteeth.bedrock.core.test.tcp;

import com.github.rabbitnoteeth.bedrock.core.server.tcp.TcpServer;
import com.github.rabbitnoteeth.bedrock.core.server.tcp.TcpServerBuilder;
import com.github.rabbitnoteeth.bedrock.core.server.tcp.handler.TcpServerHandler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServerDemo.class);

    public static void main(String[] args) {

        // create options (this is optional)
        NetServerOptions options = new NetServerOptions();
        // config the options...

        // create server
        TcpServer server = new TcpServerBuilder(8080)
            .setHandler(new MyTcpServerHandler())
            .setDeploymentHandler(new MyTcpServerDeploymentHandler())
            .setOptions(options)
            .build();

        // start server
        server.start();

    }

}
