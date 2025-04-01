package com.gitee.rabbitnoteeth.bedrock.core.test.http.route;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.annotation.RoutePath;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.route.HttpTemplateRoute;

@RoutePath("/demo")
public class TemplateRoute implements HttpTemplateRoute {

    @RoutePath("/index")
    public String thymeleaf(HttpContext context) {
        context.put("content", "this is index");
        return "index";
    }

}
