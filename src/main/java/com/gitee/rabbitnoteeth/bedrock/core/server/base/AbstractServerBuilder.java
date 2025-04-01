package com.gitee.rabbitnoteeth.bedrock.core.server.base;

import com.gitee.rabbitnoteeth.bedrock.core.server.base.handler.ServerDeploymentHandler;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;


public abstract class AbstractServerBuilder<T extends AbstractServerBuilder<T>> {

    protected final String host;
    protected final int port;
    protected Vertx vertx;
    protected DeploymentOptions deploymentOptions;
    protected ServerDeploymentHandler deploymentHandler;

    protected AbstractServerBuilder(int port) {
        this.host = "127.0.0.1";
        this.port = port;
    }

    protected AbstractServerBuilder(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public abstract AbstractServer build();

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public T setVertx(Vertx vertx) {
        this.vertx = vertx;
        return (T) this;
    }

    public DeploymentOptions getDeploymentOptions() {
        return deploymentOptions;
    }

    public T setDeploymentOptions(DeploymentOptions deploymentOptions) {
        this.deploymentOptions = deploymentOptions;
        return (T) this;
    }

    public ServerDeploymentHandler getDeploymentHandler() {
        return deploymentHandler;
    }

    public T setDeploymentHandler(ServerDeploymentHandler deploymentHandler) {
        this.deploymentHandler = deploymentHandler;
        return (T) this;
    }

}
