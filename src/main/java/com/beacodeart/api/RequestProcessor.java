package com.beacodeart.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class RequestProcessor implements Request.Visitor<byte[]> {

    @Override
    public byte[] visitGetRequest(String url) {
        // load index.html
        if (url.length() > 1) {
            try (FileInputStream htmlFile = new FileInputStream(
                    "D:\\documents\\fdm\\vscodews\\JavaSocketAPI\\src\\main\\java\\com\\beacodeart\\api\\"
                            + url.substring(1))) {

                // turn html into bytes
                // set content type
                byte[] one = "HTTP/1.1 200 OK\r\n\r\n".getBytes();
                byte[] two = htmlFile.readAllBytes();
                byte[] combined = new byte[one.length + two.length];
                ByteBuffer buffer = ByteBuffer.wrap(combined);
                buffer.put(one);
                buffer.put(two);
                combined = buffer.array();
                return combined;
            } catch (IOException e) {
                return "HTTP/1.1 404 NOT FOUND".getBytes();
            }
        } else {
            try (FileInputStream htmlFile = new FileInputStream(
                    "D:\\documents\\fdm\\vscodews\\JavaSocketAPI\\src\\main\\java\\com\\beacodeart\\api\\index.html")) {
                byte[] one = "HTTP/1.1 200 OK\r\n\r\n".getBytes();
                byte[] two = htmlFile.readAllBytes();
                byte[] combined = new byte[one.length + two.length];
                ByteBuffer buffer = ByteBuffer.wrap(combined);
                buffer.put(one);
                buffer.put(two);
                combined = buffer.array();
                return combined;
            } catch (IOException e) {
                e.getMessage();
            }
        }

        return "HTTP/1.1 404 NOT FOUND".getBytes();
    }

    @Override
    public byte[] visitPostRequest() {

        return "HTTP/1.1 200 OK\r\n\r\nPost Request".getBytes();
    }

    @Override
    public byte[] visitPutRequest() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitPutRequest'");
    }

    @Override
    public byte[] visitDeleteRequest() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitDeleteRequest'");
    }

}
