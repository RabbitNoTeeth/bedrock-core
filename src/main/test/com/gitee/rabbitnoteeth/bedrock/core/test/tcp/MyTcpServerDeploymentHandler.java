package com.gitee.rabbitnoteeth.bedrock.core.test.tcp;

import com.gitee.rabbitnoteeth.bedrock.core.server.base.handler.ServerDeploymentHandler;
import com.gitee.rabbitnoteeth.bedrock.core.server.tcp.handler.TcpServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTcpServerDeploymentHandler implements ServerDeploymentHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServerHandler.class);

    @Override
    public void onStart(String serverId) {
        LOGGER.info("server start");
    }

    @Override
    public void onStartFailed(String serverId, Throwable e) {
        LOGGER.error("server start failed", e);
    }

    @Override
    public void onStop(String serverId) {
        LOGGER.info("server stop");
    }

    @Override
    public void onStopFailed(String serverId, Throwable e) {
        LOGGER.error("server stop failed", e);
    }
}
