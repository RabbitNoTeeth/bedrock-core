package com.github.rabbitnoteeth.bedrock.core.server.http.context;

import com.github.rabbitnoteeth.bedrock.core.server.http.constant.HttpConstants;
import com.github.rabbitnoteeth.bedrock.core.server.http.context.request.*;
import com.github.rabbitnoteeth.bedrock.core.server.http.entity.HttpMethod;
import com.github.rabbitnoteeth.bedrock.core.server.http.session.HttpSession;
import com.github.rabbitnoteeth.bedrock.util.FileUtils;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.Cookie;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

import java.util.*;

public class HttpContext {

    private final String RESPONSE_DATA_KEY = "INTERNAL_RESPONSE_DATA";
    private final RoutingContext routingContext;
    private boolean interrupted;
    private boolean redirected;

    public HttpContext(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    public Vertx vertx() {
        return routingContext.vertx();
    }

    public void put(String key, Object obj) {
        routingContext.put(key, obj);
    }

    public <T> T get(String key) {
        return routingContext.get(key);
    }

    public <T> T get(String key, T defaultValue) {
        return routingContext.get(key, defaultValue);
    }

    public <T> T remove(String key) {
        return routingContext.remove(key);
    }

    public void setResponseData(Object data) {
        routingContext.put(RESPONSE_DATA_KEY, data);
    }

    public Object getResponseData() {
        return routingContext.get(RESPONSE_DATA_KEY);
    }

    public String getPathParam(String key) {
        return routingContext.pathParam(key);
    }

    public String getRequestId() {
        return routingContext.get(HttpConstants.REQUEST_ID);
    }

    public String getRequestPath() {
        return routingContext.request().path();
    }

    public HttpMethod getRequestMethod() {
        String name = routingContext.request().method().name();
        return HttpMethod.valueOf(name.toUpperCase());
    }

    public HttpRequestParams getRequestParams() {
        return new HttpRequestParams(routingContext.request().params());
    }

    public HttpRequestHeaders getRequestHeaders() {
        return new HttpRequestHeaders(routingContext.request().headers());
    }

    public HttpRequestBody getRequestBody() {
        return new HttpRequestBody(routingContext.body());
    }

    public HttpRequestFiles getRequestFiles() throws Exception {
        List<HttpRequestFile> files = new ArrayList<>();
        List<FileUpload> fileUploads = routingContext.fileUploads();
        if (fileUploads == null || fileUploads.isEmpty()) {
            return new HttpRequestFiles(files);
        }
        for (FileUpload f : fileUploads) {
            HttpRequestFile file = new HttpRequestFile();
            String fileName = f.fileName();
            int index = fileName.lastIndexOf(".");
            if (index == -1) {
                file.setFileName(fileName);
                file.setFileExtension("");
            } else {
                file.setFileName(fileName.substring(0, index));
                file.setFileExtension(fileName.substring(index));
            }
            file.setBytes(FileUtils.readFile("./" + f.uploadedFileName()));
            files.add(file);
        }
        return new HttpRequestFiles(files);
    }

    public SocketAddress getRequestSocketAddress() {
        return routingContext.request().localAddress();
    }

    public HttpSession getSession() {
        Session session = routingContext.session();
        return session instanceof HttpSession ? (HttpSession) session : null;
    }

    public Cookie getCookie(String name) {
        return routingContext.request().getCookie(name);
    }

    public Cookie getCookie(String name, String domain, String path) {
        return routingContext.request().getCookie(name, domain, path);
    }

    public int cookieCount() {
        return routingContext.request().cookieCount();
    }

    public Set<Cookie> cookies(String name) {
        return routingContext.request().cookies(name);
    }

    public Set<Cookie> cookies() {
        return routingContext.request().cookies();
    }

    public void addCookie(Cookie cookie) {
        routingContext.response().addCookie(cookie);
    }

    public Cookie removeCookie(String name) {
        return routingContext.response().removeCookie(name);
    }

    public Set<Cookie> removeCookies(String name) {
        return routingContext.response().removeCookies(name);
    }

    public Cookie removeCookie(String name, String domain, String path) {
        return routingContext.response().removeCookie(name);
    }

    public void putHeader(String name, String value) {
        routingContext.response().putHeader(name, value);
    }

    public void setStatusCode(int code) {
        routingContext.response().setStatusCode(code);
    }

    public void interrupt() {
        this.interrupted = true;
    }

    public void interrupt(String chunk) {
        routingContext.response().write(chunk);
        this.interrupted = true;
    }

    public void interrupt(Buffer buffer) {
        routingContext.response().write(buffer);
        this.interrupted = true;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void redirect(String url) {
        routingContext.redirect(url);
        this.redirected = true;
    }

    public boolean isRedirected() {
        return redirected;
    }

    public boolean isEnded() {
        return routingContext.response().ended() || routingContext.response().closed();
    }
}
