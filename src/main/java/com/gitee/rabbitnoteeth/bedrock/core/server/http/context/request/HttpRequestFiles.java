package com.gitee.rabbitnoteeth.bedrock.core.server.http.context.request;

import java.util.List;
import java.util.Optional;

public class HttpRequestFiles {

    private final List<HttpRequestFile> files;

    public HttpRequestFiles(List<HttpRequestFile> files) {
        this.files = files;
    }

    public List<HttpRequestFile> getFiles() {
        return files;
    }

    public HttpRequestFile getFile(String filename) {
        Optional<HttpRequestFile> optional = files.stream().filter(f -> filename.equals(f.getFileName())).findFirst();
        return optional.orElse(null);
    }

    public boolean isEmpty() {
        return files == null || files.isEmpty();
    }

    public int size() {
        return isEmpty() ? 0 : files.size();
    }
}
