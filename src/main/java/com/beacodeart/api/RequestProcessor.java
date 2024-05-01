package com.beacodeart.api;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.beacodeart.api.dto.BlogDTO;
import com.beacodeart.api.dto.ReplyDTO;
import com.beacodeart.api.models.User;
import com.beacodeart.api.repositories.BlogRepository;
import com.beacodeart.api.repositories.UserRepository;
import com.beacodeart.api.repositories.ReplyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//impliment singleton for repositoriers?
public class RequestProcessor implements Request.Visitor<byte[]> {

    ObjectMapper mapper;

    public RequestProcessor(ObjectMapper mapper) {
        super();
        this.mapper = mapper;
    }

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
                    case "blogs":
                        twoListOfStrings = getBlogs(url);
                        break;
                    case "replies":
                        twoListOfStrings = getReplies(url);
                        break;
                    default:
                        twoListOfStrings.add("HELLO");
                        break;
                }

                String line1 = "HTTP/1.1 200 OK\r\n";

                String contentlength = "Content-length: ";
                String contenttype = "Content-type: application/json \r\n\r\n";


                //see if this can be done using object mapper
                String two = "[";

                int i = 0;
                while (i < twoListOfStrings.size() - 1) {
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

    private List<String> getReplies(String url) {
        return ReplyRepository.getResource(url);
    }

    private List<String> getUsers(String url) {
        return UserRepository.getResource(url);
    }

    private List<String> getBlogs(String url) {
        return BlogRepository.getResource(url);
    }

    @Override
    public byte[] visitPostRequest(String url, String body) {
        switch (url.substring(1)) {
            case "users":
                postUser(body);
                break;
            case "blogs":
                postBlog(body);
                break;
            case "replies":
                postReply(body);
                break;
            default:
                break;
        }

        return "HTTP/1.1 200 OK\r\n\r\nHello".getBytes();
    }

    private String postReply(String body) {
        try {
            ReplyDTO reply1 = mapper.readValue(body, ReplyDTO.class);
            return ReplyRepository.postResource(reply1);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    private String postBlog(String body) {
        try {
            BlogDTO blog1 = mapper.readValue(body, BlogDTO.class);
            return BlogRepository.postResource(blog1);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return null;
    }

    private String postUser(String body) {
        try {
            User user1 = mapper.readValue(body, User.class);
            return UserRepository.postResource(user1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;

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
