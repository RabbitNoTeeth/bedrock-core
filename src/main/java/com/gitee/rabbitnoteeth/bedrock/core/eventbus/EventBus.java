package com.gitee.rabbitnoteeth.bedrock.core.eventbus;

import com.gitee.rabbitnoteeth.bedrock.core.BedrockCore;

import java.util.function.Consumer;

public class EventBus {

    private static final io.vertx.core.eventbus.EventBus BUS = BedrockCore.getVertx().eventBus();

    private EventBus() {
    }

    public static void send(String eventName, String data) {
        BUS.send(eventName, data);
    }

    public static void publish(String eventName, String data) {
        BUS.publish(eventName, data);
    }

    public static void listen(String eventName, Consumer<String> consumer) {
        BUS.consumer(eventName, message -> {
            consumer.accept(message.body().toString());
        });
    }

}
