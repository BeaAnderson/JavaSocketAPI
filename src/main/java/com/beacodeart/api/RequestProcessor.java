package com.beacodeart.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class RequestProcessor implements Request.Visitor<byte[]> {

    @Override
    public byte[] visitGetRequest(String url) {
        // load index.html
        if (url.length() > 1) {
            try (FileInputStream htmlFile = new FileInputStream(
                    "src\\main\\resources\\"
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
                    "src\\main\\resources\\index.html")) {
                Repository repo = new Repository();
                byte[] one = "HTTP/1.1 200 OK\r\n\r\n".getBytes();
                String twoString = repo.getResource();
                byte[] two = twoString.getBytes();
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
    public byte[] visitPostRequest(String url, String body) {
        String path = "src\\main\\resources\\" + url.substring(1) + ".txt";
        try {
            Files.write(Paths.get(path), body.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            return "HTTP/1.1 404 NOT FOUND".getBytes();
        }
        return "HTTP/1.1 200 OK\r\n\r\n".getBytes();
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
