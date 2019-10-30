package com.didi.chameleon.sdk.common.http;

import com.didi.chameleon.sdk.adapter.http.CmlRequest;

import java.util.HashMap;
import java.util.Map;

class CmlOptions {
    private String method;
    private String url;
    private Map<String, String> headers;
    private String body;
    private Type type;
    private int timeout;

    private CmlOptions(String method,
                       String url,
                       Map<String, String> headers,
                       String body,
                       Type type,
                       int timeout) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.type = type;
        if (timeout == 0) {
            timeout = CmlRequest.DEFAULT_TIMEOUT_MS;
        }
        this.timeout = timeout;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Type getType() {
        return type;
    }

    public int getTimeout() {
        return timeout;
    }

    public enum Type {
        json, text, jsonp
    }

    public static class Builder {
        private String method;
        private String url;
        private Map<String, String> headers = new HashMap<>();
        private String body;
        private Type type;
        private int timeout;

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder putHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        /**
         * default text
         * json = jsonp
         *
         * @param type
         * @return
         */
        public Builder setType(String type) {
            if (Type.json.name().equals(type)) {
                this.type = Type.json;
            } else if (Type.jsonp.name().equals(type)) {
                this.type = Type.jsonp;
            } else {
                this.type = Type.text;
            }
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public CmlOptions createOptions() {
            return new CmlOptions(method, url, headers, body, type, timeout);
        }
    }
}
