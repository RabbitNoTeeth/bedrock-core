package com.gitee.rabbitnoteeth.bedrock.core.test.http.route;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.annotation.*;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.request.HttpRequestBody;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.request.HttpRequestHeaders;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.request.HttpRequestParams;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.route.HttpRestRoute;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.session.HttpSession;
import com.gitee.rabbitnoteeth.bedrock.core.test.http.entity.Aaa;
import com.gitee.rabbitnoteeth.bedrock.util.validation.annotation.Validate;
import com.gitee.rabbitnoteeth.bedrock.util.validation.annotation.ValidateBean;
import com.gitee.rabbitnoteeth.bedrock.util.validation.entity.Rule;

import java.util.List;

@RoutePath("/demo")
public class RestRoute implements HttpRestRoute {

    @RoutePath("/test")
    public String test(HttpContext httpContext,
                    HttpSession httpSession,
                    HttpRequestParams requestParams,
                    HttpRequestHeaders httpRequestHeaders,
                    HttpRequestBody httpRequestBody,
                    @RequestHeader("User-Agent") String userAgent,
                    @RequestParam("asd") String asd,
                    @RequestBody String xxx,
                    @RequestBody(attr = "aaa") String aaa,
                    @RequestBody(attr = "bbb", isArray = true) List<String> bbb,
                    @RequestData @ValidateBean Aaa data) {
        System.out.println("RequestHeader: User-Agent=" + userAgent);
        System.out.println("RequestParam: asd=" + asd);
        System.out.println("RequestBody: " + xxx);
        System.out.println("RequestBody: aaa=" + aaa);
        System.out.println("RequestBody: bbb=" + bbb);
        System.out.println("RequestData: data=" + data);
        return "ok";
    }

}
