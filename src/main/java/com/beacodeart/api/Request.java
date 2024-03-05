package com.beacodeart.api;

import java.util.Map;

abstract class Request {
    String url;
    Map<String, String> headers;

    interface Visitor<R> {
        R visitGetRequest();

        R visitPostRequest();

        R visitPutRequest();

        R visitDeleteRequest();
    }

    static class GetRequest extends Request {

        public GetRequest(String url, Map<String, String> headers) {
            this.url = url;
            this.headers = headers;

        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetRequest();
        }
    }

    static class PostRequest extends Request {
        private String body;

        public PostRequest(String url, Map<String, String> headers, String body) {
            this.url = url;
            this.headers = headers;
            this.body = body;
        }

        public String getBody() {
            return body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPostRequest();
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getUrl() {
        return url;
    }

    abstract <R> R accept(Visitor<R> visitor);

}
