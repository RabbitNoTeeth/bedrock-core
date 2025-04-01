package com.gitee.rabbitnoteeth.bedrock.core.server.base;

import com.gitee.rabbitnoteeth.bedrock.core.server.base.handler.ServerDeploymentHandler;
import com.gitee.rabbitnoteeth.bedrock.util.StringUtils;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public abstract class AbstractServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServer.class);
    protected final String id;
    protected final String host;
    protected final int port;
    protected String deployId;
    protected final Vertx vertx;
    protected final DeploymentOptions deploymentOptions;
    protected final ServerDeploymentHandler deploymentHandler;

    protected AbstractServer(String host,
                             int port,
                             Vertx vertx,
                             DeploymentOptions deploymentOptions,
                             ServerDeploymentHandler deploymentHandler) {
        this.deploymentHandler = deploymentHandler;
        this.id = this.name() + "-SERVER-" + System.nanoTime();
        this.host = StringUtils.isBlank(host) ? "127.0.0.1" : host;
        this.port = port;
        this.vertx = vertx;
        this.deploymentOptions = deploymentOptions;
    }

    public final String id() {
        return id;
    }

    public final String host() {
        return host;
    }

    public final int port() {
        return port;
    }

    /**
     * the name of server
     * @return
     */
    public abstract String name();

    /**
     * the reference of server
     * @return
     */
    public abstract String verticleReference();

    /**
     * start the server
     */
    public final void start() {
        this.deploymentOptions.setConfig(new JsonObject().put("id", this.id));
        this.vertx.deployVerticle(this.verticleReference(), this.deploymentOptions, res -> {
            if (res.succeeded()) {
                if (this.deploymentHandler != null) {
                    this.deploymentHandler.onStart(this.id);
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("{} server started at '{}:{}'", this.name(), this.host, this.port);
                }
            } else {
                if (this.deploymentHandler != null) {
                    this.deploymentHandler.onStartFailed(this.id, res.cause());
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.error("Failed to start {} server at '{}:{}'", this.name(), this.host, this.port, res.cause());
                }
            }
        });
    }

    /**
     * stop the server
     */
    public final void stop() {
        if (StringUtils.isBlank(this.deployId)) {
            if (this.deploymentHandler != null) {
                this.deploymentHandler.onStop(this.id);
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} server stopped at '{}:{}'", this.name(), this.host, this.port);
            }
            return;
        }
        this.vertx.undeploy(this.deployId, res -> {
            if (res.succeeded()) {
                this.deployId = null;
                if (this.deploymentHandler != null) {
                    this.deploymentHandler.onStop(this.id);
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("{} server stopped at '{}:{}'", this.name(), this.host, this.port);
                }
            } else {
                if (this.deploymentHandler != null) {
                    this.deploymentHandler.onStopFailed(this.id, res.cause());
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.error("Failed to stop {} server at '{}:{}'", this.name(), this.host, this.port, res.cause());
                }
            }
        });
    }

}
