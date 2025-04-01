package com.gitee.rabbitnoteeth.bedrock.core.server.http.annotation;


import com.gitee.rabbitnoteeth.bedrock.core.server.http.entity.HttpMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoutePath {

    String value();

    HttpMethod method() default HttpMethod.ANY;

}
