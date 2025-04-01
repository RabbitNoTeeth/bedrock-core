package com.gitee.rabbitnoteeth.bedrock.core.server.http.route;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.route.entity.HttpRouteJointPoint;

public interface HttpRouteInterceptor {

    int order();

    default void before(HttpRouteJointPoint jointPoint) throws Throwable {
    }

    default void after(HttpRouteJointPoint jointPoint) throws Throwable {
    }

    default void afterThrowing(HttpRouteJointPoint jointPoint, Throwable e) throws Throwable {
    }

}
