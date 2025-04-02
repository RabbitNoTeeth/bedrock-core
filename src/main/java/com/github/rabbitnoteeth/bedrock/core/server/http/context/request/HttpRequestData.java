package com.github.rabbitnoteeth.bedrock.core.server.http.context.request;

import io.vertx.core.MultiMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

class HttpRequestData {

    protected final MultiMap map;

    protected HttpRequestData(MultiMap map) {
        this.map = map;
    }

    public String get(CharSequence name) {
        return map.get(name);
    }

    public String get(String name) {
        return map.get(name);
    }

    public String get(CharSequence name, boolean caseInsensitive) {
        if (caseInsensitive) {
            return get(name);
        }
        List<Map.Entry<String, String>> entries = entries();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getKey().equalsIgnoreCase(name.toString())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String get(String name, boolean caseInsensitive) {
        if (caseInsensitive) {
            return get(name);
        }
        List<Map.Entry<String, String>> entries = entries();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public List<String> getAll(String name) {
        return map.getAll(name);
    }

    public List<String> getAll(CharSequence name) {
        return map.getAll(name);
    }

    public void forEach(BiConsumer<String, String> action) {
        map.forEach(action);
    }

    public List<Map.Entry<String, String>> entries() {
        return map.entries();
    }

    public boolean contains(String name) {
        return map.contains(name);
    }

    public boolean contains(CharSequence name) {
        return map.contains(name);
    }

    public boolean contains(String name, boolean caseInsensitive) {
        if (caseInsensitive) {
            return contains(name);
        }
        return names().stream().anyMatch(n -> n.equalsIgnoreCase(name));
    }

    public boolean contains(CharSequence name, boolean caseInsensitive) {
        if (caseInsensitive) {
            return contains(name);
        }
        return names().stream().anyMatch(n -> n.equalsIgnoreCase(name.toString()));
    }

    public boolean contains(String name, String value, boolean caseInsensitive) {
        return map.contains(name, value, caseInsensitive);
    }

    public boolean contains(CharSequence name, CharSequence value, boolean caseInsensitive) {
        return map.contains(name, value, caseInsensitive);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set<String> names() {
        return map.names();
    }

    public int size() {
        return map.size();
    }
}
