package com.beacodeart.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class RequestProcessor implements Request.Visitor<byte[]> {

    @Override
    public byte[] visitGetRequest(String url) {
        // load index.html
        if (url.length() <= 1) {
            try {
                String line1 = "HTTP/1.1 200 OK\r\n\r\nHello world";
                return line1.getBytes();
                
            } catch (Exception e) {
                return "HTTP/1.1 404 NOT FOUND".getBytes();
            }
        } else {
            try {
                List<String> twoListOfStrings = new ArrayList<String>();
                switch (url.split("/")[1]) {
                    case "users":
                        twoListOfStrings = getUsers(url);
                        break;
                
                    default:
                        twoListOfStrings.add("HELLO");
                        break;
                }
                
                String line1 = "HTTP/1.1 200 OK\r\n";

                String contentlength = "Content-length: ";
                String contenttype = "Content-type: application/json \r\n\r\n";

                String two = "[";

                int i=0;
                while (i<twoListOfStrings.size()-1){
                    two += twoListOfStrings.get(i);
                    two += ",";
                    i++;
                }

                two += twoListOfStrings.get(i);


                two += "]";
                byte[] twoBytes = two.getBytes();
                contentlength += (Integer.toString(twoBytes.length) + "\r\n");
                String headers = line1 + contentlength + contenttype;
                byte[] oneBytes = headers.getBytes();
                
                byte[] combined = new byte[oneBytes.length + twoBytes.length];
                ByteBuffer buffer = ByteBuffer.wrap(combined);
                buffer.put(oneBytes);
                buffer.put(twoBytes);
                combined = buffer.array();
                return combined;
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return "HTTP/1.1 404 NOT FOUND".getBytes();
    }

    private List<String> getUsers(String url) {
        UserRepository userRepository = new UserRepository();
        return userRepository.getResource(url);
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
