package com.gitee.rabbitnoteeth.bedrock.core.server.http.route.entity;

import com.gitee.rabbitnoteeth.bedrock.core.server.http.annotation.*;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.HttpContext;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.context.request.*;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.entity.HttpMethod;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.route.HttpRouteInterceptor;
import com.gitee.rabbitnoteeth.bedrock.core.server.http.session.HttpSession;
import com.gitee.rabbitnoteeth.bedrock.util.DateUtils;
import com.gitee.rabbitnoteeth.bedrock.util.JsonUtils;
import com.gitee.rabbitnoteeth.bedrock.util.StringUtils;
import com.gitee.rabbitnoteeth.bedrock.util.validation.ValidationUtils;
import com.gitee.rabbitnoteeth.bedrock.util.validation.annotation.Validate;
import com.gitee.rabbitnoteeth.bedrock.util.validation.annotation.ValidateBean;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HttpRouteEndpoint {

    private final String path;
    private final HttpMethod method;
    private final Class<?> targetClass;
    private final Method targetMethod;
    private final Object target;
    private final List<HttpRouteInterceptor> interceptors;

    public HttpRouteEndpoint(String path,
                             HttpMethod method,
                             Class<?> targetClass,
                             Method targetMethod,
                             Object target,
                             List<HttpRouteInterceptor> interceptors) {
        this.path = path;
        this.method = method;
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.target = target;
        this.interceptors = interceptors;
    }

    public String getPath() {
        return path;
    }

    public Object execute(HttpContext httpContext) throws Throwable {
        HttpRouteJointPoint jointPoint = new HttpRouteJointPoint(path, targetClass, targetMethod, httpContext);
        ;
        try {
            Object[] args = createArgs(httpContext);
            jointPoint.setTargetMethodArgs(args);
            if (!interceptors.isEmpty()) {
                List<HttpRouteInterceptor> beforeInterceptors = interceptors.stream().sorted(Comparator.comparingInt(HttpRouteInterceptor::order)).toList();
                for (HttpRouteInterceptor interceptor : beforeInterceptors) {
                    if (!isHttpContextValid(httpContext)) {
                        return null;
                    }
                    interceptor.before(jointPoint);
                }
            }
            if (!isHttpContextValid(httpContext)) {
                return null;
            }
            Object result = targetMethod.invoke(target, args);
            jointPoint.setResult(result);
            if (!interceptors.isEmpty()) {
                List<HttpRouteInterceptor> afterInterceptors = interceptors.stream().sorted(Comparator.comparingInt(HttpRouteInterceptor::order)).toList().reversed();
                for (HttpRouteInterceptor interceptor : afterInterceptors) {
                    if (!isHttpContextValid(httpContext)) {
                        return null;
                    }
                    interceptor.after(jointPoint);
                }
            }
            if (!isHttpContextValid(httpContext)) {
                return null;
            }
            return result;
        } catch (Throwable e) {
            Throwable targetThrowable = e;
            if (targetThrowable instanceof InvocationTargetException) {
                targetThrowable = ((InvocationTargetException) targetThrowable).getTargetException();
            }
            if (!interceptors.isEmpty()) {
                List<HttpRouteInterceptor> afterThrowingInterceptors = interceptors.stream().sorted(Comparator.comparingInt(HttpRouteInterceptor::order)).toList().reversed();
                for (HttpRouteInterceptor interceptor : afterThrowingInterceptors) {
                    if (!isHttpContextValid(httpContext)) {
                        return null;
                    }
                    targetThrowable = executeInterceptorAfterThrowing(interceptor, jointPoint, targetThrowable);
                }
            }
            if (!isHttpContextValid(httpContext)) {
                return null;
            }
            throw targetThrowable;
        }
    }

    private Throwable executeInterceptorAfterThrowing(HttpRouteInterceptor interceptor, HttpRouteJointPoint jointPoint, Throwable e) {
        try {
            interceptor.afterThrowing(jointPoint, e);
            return e;
        } catch (Throwable e_) {
            return e_;
        }
    }

    private boolean isHttpContextValid(HttpContext httpContext) {
        return !httpContext.isRedirected() && !httpContext.isInterrupted() && !httpContext.isEnded();
    }

    private Object[] createArgs(HttpContext httpContext) throws Throwable {
        Parameter[] parameters = targetMethod.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            args[i] = createArg(parameter, httpContext);
        }
        return args;
    }

    private Object createArg(Parameter parameter, HttpContext httpContext) throws Throwable {
        Object val = mappingInternalType(parameter, httpContext);
        if (val != null) {
            return val;
        }
        PathParam pathParam = parameter.getDeclaredAnnotation(PathParam.class);
        if (pathParam != null) {
            return mappingPathParamType(parameter, httpContext, pathParam);
        }
        RequestFile requestFile = parameter.getDeclaredAnnotation(RequestFile.class);
        if (requestFile != null) {
            return mappingRequestFileType(parameter, httpContext, requestFile);
        }
        Object result = null;
        RequestHeader requestHeader = parameter.getDeclaredAnnotation(RequestHeader.class);
        if (requestHeader != null) {
            result = mappingRequestHeaderType(parameter, httpContext, requestHeader);
        }
        RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
        if (requestParam != null) {
            result = mappingRequestParamType(parameter, httpContext, requestParam);
        }
        RequestBody requestBody = parameter.getDeclaredAnnotation(RequestBody.class);
        if (requestBody != null) {
            result = mappingRequestBodyType(parameter, httpContext, requestBody);
        }
        RequestData requestData = parameter.getDeclaredAnnotation(RequestData.class);
        if (requestData != null) {
            result = mappingRequestDataType(parameter, httpContext);
        }
        Annotation[] annotations = parameter.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof ValidateBean) {
                ValidationUtils.validate(result);
                break;
            }
            if (annotation instanceof Validate) {
                ValidationUtils.validate((Validate) annotation, result);
            }
        }
        return result;
    }

    private Object mappingInternalType(Parameter parameter, HttpContext httpContext) throws Throwable {
        Class<?> parameterType = parameter.getType();
        if (HttpContext.class.equals(parameterType)) {
            return httpContext;
        }
        if (HttpSession.class.equals(parameterType)) {
            return httpContext.getSession();
        }
        if (HttpRequestHeaders.class.equals(parameterType)) {
            return httpContext.getRequestHeaders();
        }
        if (HttpRequestParams.class.equals(parameterType)) {
            return httpContext.getRequestParams();
        }
        if (HttpRequestBody.class.equals(parameterType)) {
            return httpContext.getRequestBody();
        }
        if (HttpRequestFiles.class.equals(parameterType)) {
            return httpContext.getRequestFiles();
        }
        return null;
    }

    private Object mappingPathParamType(Parameter parameter, HttpContext httpContext, PathParam annotation) throws Throwable {
        Class<?> parameterType = parameter.getType();
        String paramName = annotation.value();
        String paramValue = httpContext.getPathParam(paramName);
        return createArgValue(paramValue, parameterType);
    }

    private Object mappingRequestHeaderType(Parameter parameter, HttpContext httpContext, RequestHeader annotation) throws Throwable {
        Class<?> parameterType = parameter.getType();
        String paramName = annotation.value();
        HttpRequestHeaders requestHeaders = httpContext.getRequestHeaders();
        if (requestHeaders.isEmpty() || !requestHeaders.contains(paramName)) {
            return null;
        }
        String paramValue = requestHeaders.get(paramName);
        return createArgValue(paramValue, parameterType);
    }

    private Object mappingRequestParamType(Parameter parameter, HttpContext httpContext, RequestParam annotation) throws Throwable {
        Class<?> parameterType = parameter.getType();
        String paramName = annotation.value();
        HttpRequestParams requestParams = httpContext.getRequestParams();
        if (requestParams.isEmpty() || !requestParams.contains(paramName)) {
            return null;
        }
        String paramValue = requestParams.get(paramName);
        return createArgValue(paramValue, parameterType);
    }

    private Object mappingRequestBodyType(Parameter parameter, HttpContext httpContext, RequestBody annotation) throws Throwable {
        Class<?> parameterType = parameter.getType();
        HttpRequestBody requestBody = httpContext.getRequestBody();
        if (requestBody.isEmpty()) {
            return null;
        }
        String attr = annotation.attr();
        boolean isArray = annotation.isArray();
        JsonElement bodyJson = requestBody.asJson();
        if (!isArray) {
            if (StringUtils.isBlank(attr)) {
                return createArgValue(bodyJson, parameterType);
            } else {
                if (!bodyJson.isJsonObject()) {
                    return null;
                } else {
                    JsonElement value = bodyJson.getAsJsonObject().get(attr);
                    return value == null || value.isJsonNull() ? null : createArgValue(value, parameterType);
                }
            }
        } else {
            if (!List.class.equals(parameterType)) {
                return null;
            }
            Class<?> typeArgument = getTypeArgument(parameter);
            if (typeArgument == null) {
                return null;
            }
            if (StringUtils.isBlank(attr)) {
                if (!bodyJson.isJsonArray()) {
                    return null;
                }
                return createArgListValue(typeArgument, bodyJson.getAsJsonArray());
            } else {
                if (!bodyJson.isJsonObject()) {
                    return null;
                } else {
                    JsonElement value = bodyJson.getAsJsonObject().get(attr);
                    if (value == null || !value.isJsonArray()) {
                        return null;
                    }
                    return createArgListValue(typeArgument, value.getAsJsonArray());
                }
            }
        }
    }

    private Object mappingRequestDataType(Parameter parameter, HttpContext httpContext) throws Throwable {
        Class<?> parameterType = parameter.getType();
        Constructor<?> constructor;
        try {
            constructor = parameterType.getConstructor();
        } catch (NoSuchMethodException e) {
            return null;
        }
        HttpRequestParams requestParams = httpContext.getRequestParams();
        HttpRequestBody requestBody = httpContext.getRequestBody();
        Object instance = constructor.newInstance();
        Field[] fields = parameterType.getDeclaredFields();
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            String fieldName = field.getName();
            if (requestParams.contains(fieldName)) {
                field.setAccessible(true);
                String value = requestParams.get(fieldName);
                field.set(instance, createArgValue(value, fieldType));
                continue;
            }
            if (requestBody.isEmpty()) {
                continue;
            }
            JsonElement bodyJson = requestBody.asJson();
            if (!bodyJson.isJsonObject()) {
                continue;
            }
            JsonObject jsonObject = bodyJson.getAsJsonObject();
            if (!jsonObject.has(fieldName)) {
                continue;
            }
            JsonElement element = jsonObject.get(fieldName);
            if (element.isJsonNull()) {
                continue;
            }
            if (List.class.equals(fieldType)) {
                if (!element.isJsonArray()) {
                    continue;
                }
                Class<?> typeArgument = getTypeArgument(field);
                if (typeArgument == null) {
                    continue;
                }
                field.setAccessible(true);
                field.set(instance, createArgListValue(typeArgument, element.getAsJsonArray()));
            } else {
                field.setAccessible(true);
                field.set(instance, createArgValue(element, fieldType));
            }
        }
        return instance;
    }

    private Class<?> getTypeArgument(Field field) {
        Type parameterizedType = field.getGenericType();
        if (!(parameterizedType instanceof ParameterizedType)) {
            return null;
        }
        Type[] types = ((ParameterizedType) parameterizedType).getActualTypeArguments();
        if (types == null || types.length == 0) {
            return null;
        }
        return (Class<?>) types[0];
    }

    private Class<?> getTypeArgument(Parameter parameter) {
        Type parameterizedType = parameter.getParameterizedType();
        if (!(parameterizedType instanceof ParameterizedType)) {
            return null;
        }
        Type[] types = ((ParameterizedType) parameterizedType).getActualTypeArguments();
        if (types == null || types.length == 0) {
            return null;
        }
        return (Class<?>) types[0];
    }

    private Object mappingRequestFileType(Parameter parameter, HttpContext httpContext, RequestFile annotation) throws Throwable {
        Class<?> parameterType = parameter.getType();
        if (!HttpRequestFile.class.equals(parameterType)) {
            return null;
        }
        HttpRequestFiles requestFiles = httpContext.getRequestFiles();
        if (requestFiles.isEmpty()) {
            return null;
        }
        return requestFiles.getFile(annotation.value());
    }

    private <T> List<T> createArgListValue(Class<T> tClass, JsonArray jsonArray) {
        List<T> res = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            res.add(JsonUtils.decode(element, tClass));
        }
        return res;
    }

    private Object createArgValue(String paramValue, Class<?> targetType) throws Throwable {
        if (String.class.equals(targetType)) {
            return paramValue;
        }
        if (Date.class.equals(targetType)) {
            return DateUtils.parseDate(paramValue, "yyyy-MM-dd HH:mm:ss");
        }
        if (LocalDate.class.equals(targetType)) {
            return DateUtils.parseLocalDate(paramValue, "yyyy-MM-dd");
        }
        if (LocalDateTime.class.equals(targetType)) {
            return DateUtils.parseLocalDateTime(paramValue, "yyyy-MM-dd HH:mm:ss");
        }
        return JsonUtils.decode(paramValue, targetType);
    }

    private Object createArgValue(JsonElement jsonElement, Class<?> targetType) throws Throwable {
        if (jsonElement.isJsonNull()) {
            return null;
        }
        if (String.class.equals(targetType)) {
            return jsonElement.isJsonPrimitive() ? jsonElement.getAsString() : jsonElement.toString();
        }
        if (Date.class.equals(targetType)) {
            return DateUtils.parseDate(jsonElement.toString(), "yyyy-MM-dd HH:mm:ss");
        }
        if (LocalDate.class.equals(targetType)) {
            return DateUtils.parseLocalDate(jsonElement.toString(), "yyyy-MM-dd");
        }
        if (LocalDateTime.class.equals(targetType)) {
            return DateUtils.parseLocalDateTime(jsonElement.toString(), "yyyy-MM-dd HH:mm:ss");
        }
        return JsonUtils.decode(jsonElement, targetType);
    }

    public HttpMethod getMethod() {
        return method;
    }
}
