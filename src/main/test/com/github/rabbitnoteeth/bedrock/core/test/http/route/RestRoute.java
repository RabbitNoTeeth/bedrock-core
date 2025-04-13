package com.github.rabbitnoteeth.bedrock.core.test.http.route;

import com.github.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.github.rabbitnoteeth.bedrock.core.server.http.context.request.HttpRequestBody;
import com.github.rabbitnoteeth.bedrock.core.server.http.context.request.HttpRequestHeaders;
import com.github.rabbitnoteeth.bedrock.core.server.http.context.request.HttpRequestParams;
import com.github.rabbitnoteeth.bedrock.core.server.http.route.HttpRestRoute;
import com.github.rabbitnoteeth.bedrock.core.server.http.session.HttpSession;
import com.github.rabbitnoteeth.bedrock.core.test.http.entity.Aaa;
import com.github.rabbitnoteeth.bedrock.util.validation.annotation.ValidateBean;

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
