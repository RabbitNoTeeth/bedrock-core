package com.gitee.rabbitnoteeth.bedrock.core.test.udp;

import com.gitee.rabbitnoteeth.bedrock.core.server.tcp.TcpServer;
import com.gitee.rabbitnoteeth.bedrock.core.server.tcp.TcpServerBuilder;
import com.gitee.rabbitnoteeth.bedrock.core.server.udp.UdpServer;
import com.gitee.rabbitnoteeth.bedrock.core.server.udp.UdpServerBuilder;
import com.gitee.rabbitnoteeth.bedrock.core.test.tcp.MyTcpServerDeploymentHandler;
import com.gitee.rabbitnoteeth.bedrock.core.test.tcp.MyTcpServerHandler;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.net.NetServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpServerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpServerDemo.class);

    public static void main(String[] args) {

        // create options (this is optional)
        DatagramSocketOptions options = new DatagramSocketOptions();
        // config the options...

        // create server
        UdpServer server = new UdpServerBuilder(8080)
            .setHandler(new MyUdpServerHandler())
            .setDeploymentHandler(new MyTcpServerDeploymentHandler())
            .setOptions(options)
            .build();

        // start server
        server.start();

    }

}
