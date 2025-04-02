package com.github.rabbitnoteeth.bedrock.core.server.http.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestData {

}
