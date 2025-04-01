package com.gitee.rabbitnoteeth.bedrock.core;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class BedrockCore {

    private static final Vertx INSTANCE = Vertx.vertx(
        new VertxOptions()
            .setBlockedThreadCheckInterval(30000)
            .setPreferNativeTransport(true)
    );

    private BedrockCore() {
    }

    public static Vertx getVertx() {
        return INSTANCE;
    }

}
