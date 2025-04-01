package com.gitee.rabbitnoteeth.bedrock.core.server.http.route.entity;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;

import java.lang.reflect.Method;

public class HttpRouteJointPoint {

    private final String path;
    private final Class<?> targetClass;
    private final Method targetMethod;
    private final HttpContext httpContext;
    private Object[] targetMethodArgs;
    private Object result;

    public HttpRouteJointPoint(String path,
                               Class<?> targetClass,
                               Method targetMethod,
                               HttpContext httpContext) {
        this.path = path;
        this.httpContext = httpContext;
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
    }

    public String getPath() {
        return path;
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    void setTargetMethodArgs(Object[] targetMethodArgs) {
        this.targetMethodArgs = targetMethodArgs;
    }

    public Object[] getTargetMethodArgs() {
        return targetMethodArgs;
    }

    public Object getResult() {
        return result;
    }

    void setResult(Object result) {
        this.result = result;
    }

}
