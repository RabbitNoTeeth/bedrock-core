package com.github.rabbitnoteeth.bedrock.core.server.http.handler.impl;

import com.github.rabbitnoteeth.bedrock.core.server.http.constant.HttpConstants;
import freemarker.template.TemplateNotFoundException;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;

public class TemplateHandler implements Handler<RoutingContext> {

    public static String TEMPLATE_FILE_KEY = "TEMPLATE_FILE";
    private final TemplateEngine engine;
    private final String templateDirectory;
    private final String contentType;

    public TemplateHandler(TemplateEngine engine, String templateDirectory, String contentType) {
        this.engine = engine;
        this.templateDirectory = templateDirectory != null && !templateDirectory.isEmpty() ? templateDirectory : ".";
        this.contentType = contentType;
    }

    public void handle(RoutingContext context) {
        String file = context.get(TEMPLATE_FILE_KEY).toString();
        this.engine.render(new JsonObject(context.data()), this.templateDirectory + file, (res) -> {
            if (res.succeeded()) {
                context.response().putHeader(HttpHeaders.CONTENT_TYPE, this.contentType).write(res.result());
                context.next();
            } else {
                Throwable cause = res.cause();
                if (cause instanceof TemplateNotFoundException) {
                    context.fail(HttpConstants.RESPONSE_STATUS_404);
                } else {
                    context.fail(HttpConstants.RESPONSE_STATUS_500, cause);
                }
            }
        });
    }

}
