package com.beacodeart.api;

import java.util.Map;

abstract class Request {
    String url;
    Map<String, String> headers;

    interface Visitor<R> {
        R visitGetRequest(String url);

        R visitPostRequest(String url, String body);

        R visitPutRequest(String url, String body);

        R visitDeleteRequest(String url, String body);
    }

    static class GetRequest extends Request {

        public GetRequest(String url, Map<String, String> headers) {
            this.url = url;
            this.headers = headers;

        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetRequest(url);
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
            return visitor.visitPostRequest(url, body);
        }
    }

    static class PutRequest extends Request{
        private String body;

        public PutRequest(String url, Map<String, String> headers, String body){
            this.url = url;
            this.headers = headers;
            this.body = body;
        }

        public String getBody(){
            return body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPutRequest(url, body);
        }
    }

    static class DeleteRequest extends Request{
        public String body;

        public DeleteRequest(String url, Map<String, String> headers, String body){
            this.url = url;
            this.headers = headers;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitDeleteRequest(url, body);
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
