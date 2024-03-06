package com.beacodeart.api;

public class RequestProcessor implements Request.Visitor<String> {

    @Override
    public String visitGetRequest() {

        return "HTTP/1.1 200 OK\r\n\r\nHello world";
    }

    @Override
    public String visitPostRequest() {

        return "HTTP/1.1 200 OK\r\n\r\nPost Request";
    }

    @Override
    public String visitPutRequest() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitPutRequest'");
    }

    @Override
    public String visitDeleteRequest() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitDeleteRequest'");
    }

}
