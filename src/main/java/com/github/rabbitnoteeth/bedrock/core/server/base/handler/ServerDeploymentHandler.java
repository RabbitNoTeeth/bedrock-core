package com.github.rabbitnoteeth.bedrock.core.server.base.handler;

public interface ServerDeploymentHandler {

    void onStart(String serverId);

    void onStartFailed(String serverId, Throwable e);

    void onStop(String serverId);

    void onStopFailed(String serverId, Throwable e);

}
