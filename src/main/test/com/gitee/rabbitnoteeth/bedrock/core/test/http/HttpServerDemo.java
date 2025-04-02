package com.github.rabbitnoteeth.bedrock.core.test.http;

import com.github.rabbitnoteeth.bedrock.core.server.http.HttpServer;
import com.github.rabbitnoteeth.bedrock.core.server.http.HttpServerBuilder;
import com.github.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.github.rabbitnoteeth.bedrock.core.server.http.handler.HttpErrorHandler;
import com.github.rabbitnoteeth.bedrock.core.server.http.handler.impl.DefaultErrorHandler;
import com.github.rabbitnoteeth.bedrock.core.server.http.route.HttpRestRoute;
import com.github.rabbitnoteeth.bedrock.core.server.http.route.HttpTemplateRoute;
import com.github.rabbitnoteeth.bedrock.core.server.http.route.exception.HttpRouteEndpointException;
import com.github.rabbitnoteeth.bedrock.core.server.http.template.TemplateEngine;
import com.github.rabbitnoteeth.bedrock.core.test.http.route.RestRoute;
import com.github.rabbitnoteeth.bedrock.core.test.http.route.TemplateRoute;
import com.github.rabbitnoteeth.bedrock.util.validation.exception.ValidationException;
import io.vertx.core.DeploymentOptions;

import java.util.ArrayList;
import java.util.List;

public class HttpServerDemo {

    public static void main(String[] args) {
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(Runtime.getRuntime().availableProcessors() * 2);
        HttpServer server = new HttpServerBuilder(8090)
            .setDeploymentOptions(deploymentOptions)
            .setRestRoutes(getRestRoutes())
            .setTemplateRoutes(getTemplateRoutes())
            .setTemplateEngine(TemplateEngine.THYMELEAF)
            .setAllowCors(true)
            .setErrorHandler(new HttpErrorHandler() {
                @Override
                public void onError(HttpContext context, Throwable err) throws Exception {
                    context.putHeader("Content-Type", "application/json;charset=UTF-8");
                    if (err instanceof HttpRouteEndpointException) {
                        err = err.getCause();
                        if (err instanceof ValidationException) {
                            context.setStatusCode(400);
                            context.interrupt(err.getMessage());
                            return;
                        }
                    }
                    context.setStatusCode(400);
                    context.interrupt(err.getMessage());
                }
            })
            .build();
        server.start();
    }

    private static List<HttpRestRoute> getRestRoutes() {
        List<HttpRestRoute> routes = new ArrayList<>();
        routes.add(new RestRoute());
        return routes;
    }

    private static List<HttpTemplateRoute> getTemplateRoutes() {
        List<HttpTemplateRoute> routes = new ArrayList<>();
        routes.add(new TemplateRoute());
        return routes;
    }

}
