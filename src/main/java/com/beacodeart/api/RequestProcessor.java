package com.beacodeart.api;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.beacodeart.api.dto.BlogDTO;
import com.beacodeart.api.dto.BlogUpdateDTO;
import com.beacodeart.api.dto.DeleteBlogDTO;
import com.beacodeart.api.dto.DeleteReplyDTO;
import com.beacodeart.api.dto.DeleteUserDTO;
import com.beacodeart.api.dto.ReplyDTO;
import com.beacodeart.api.dto.ReplyUpdateDTO;
import com.beacodeart.api.dto.UserUpdateDTO;
import com.beacodeart.api.models.User;
import com.beacodeart.api.repositories.BlogRepository;
import com.beacodeart.api.repositories.UserRepository;
import com.beacodeart.api.repositories.ReplyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

                // see if this can be done using object mapper
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
        String creationString = "HTTP/1.1 201 CREATED\r\nContent-Location: ";
        String key;
        HashMap<String, String> hashResponse;
        StringBuilder response3 = new StringBuilder();
        response3.append(creationString);
        switch (url.substring(1)) {
            case "users":
                hashResponse = postUser(body);
                key = (String) hashResponse.keySet().toArray()[0];
                response3.append("/users/user_id/" + key + "\r\n");
                response3.append("Content-type: application/json \r\n\r\n");
                response3.append(hashResponse.get(key));
                return response3.toString().getBytes();
            case "blogs":
                hashResponse = postBlog(body);
                key = (String) hashResponse.keySet().toArray()[0];
                response3.append("/blogs/blog_id/" + key + "\r\n");
                response3.append("Content-type: application/json \r\n\r\n");
                response3.append(hashResponse.get(key));
                return response3.toString().getBytes();
            case "replies":
                hashResponse = postReply(body);
                key = (String) hashResponse.keySet().toArray()[0];
                response3.append("/replies/reply_id/" + key + "\r\n");
                response3.append("Content-type: application/json \r\n\r\n");
                response3.append(hashResponse.get(key));
                return response3.toString().getBytes();
            default:
                break;
        }

        return creationString.getBytes();
    }

    private HashMap<String, String> postReply(String body) {
        try {
            ReplyDTO reply1 = mapper.readValue(body, ReplyDTO.class);
            return ReplyRepository.postResource(reply1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<String, String> postBlog(String body) {
        try {
            BlogDTO blog1 = mapper.readValue(body, BlogDTO.class);
            return BlogRepository.postResource(blog1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private HashMap<String, String> postUser(String body) {
        try {
            User user1 = mapper.readValue(body, User.class);
            return UserRepository.postResource(user1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public byte[] visitPutRequest(String url, String body) {
        String creationString = "HTTP/1.1 201 CREATED\r\nContent-Location: ";
        String key;
        StringBuilder response3 = new StringBuilder(creationString);
        HashMap<String, String> hashResponse;
        switch (url.split("/")[1]) {
            case "users":
                hashResponse = putUser(url, body);
                key = (String) hashResponse.keySet().toArray()[0];
                response3.append("/users/user_id/" + key + "\r\n");
                response3.append("Content-type: application/json \r\n\r\n");
                response3.append(hashResponse.get(key));
                return response3.toString().getBytes();
            case "blogs":
                hashResponse = putBlog(url, body);
                key = (String) hashResponse.keySet().toArray()[0];
                response3.append("/blogs/blog_id/" + key + "\r\n");
                response3.append("Content-type: application/json \r\n\r\n");
                response3.append(hashResponse.get(key));
                return response3.toString().getBytes();
            case "replies":
                hashResponse = putReply(url, body);
                key = (String) hashResponse.keySet().toArray()[0];
                response3.append("/replies/reply_id/" + key + "\r\n");
                response3.append("Content-type: application/json \r\n\r\n");
                response3.append(hashResponse.get(key));
                return response3.toString().getBytes();
            default:
                break;
        }
        return creationString.getBytes();
    }

    private HashMap<String, String> putReply(String url, String body) {
        try {
            ReplyUpdateDTO replyDto = mapper.readValue(body, ReplyUpdateDTO.class);
            return ReplyRepository.putResource(url, replyDto);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    private HashMap<String, String> putBlog(String url, String body) {
        try {
            BlogUpdateDTO blogDto = mapper.readValue(body, BlogUpdateDTO.class);
            return BlogRepository.putResource(url, blogDto);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    private HashMap<String, String> putUser(String url, String body) {
        try {
            UserUpdateDTO user1 = mapper.readValue(body, UserUpdateDTO.class);
            return UserRepository.putResource(url, user1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @Override
    public byte[] visitDeleteRequest(String url, String body) {
        switch (url.split("/")[1]) {
            case "users":
                deleteUser(url, body);
                break;
            case "blogs":
                deleteBlog(url, body);
                break;
            case "replies":
                deleteReply(url, body);
                break;
            default:
                break;
        }
        return "HTTP/1.1 200 OK\r\n\r\nHello".getBytes();
    }

    private String deleteReply(String url, String body) {
        try {
            DeleteReplyDTO replyDTO = mapper.readValue(body, DeleteReplyDTO.class);
            return ReplyRepository.deleteResource(url, replyDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String deleteBlog(String url, String body) {
        try {
            DeleteBlogDTO blogDTO = mapper.readValue(body, DeleteBlogDTO.class);
            return BlogRepository.deleteResource(url, blogDTO);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    private String deleteUser(String url, String body) {
        try {
            DeleteUserDTO userDTO = mapper.readValue(body, DeleteUserDTO.class);
            return UserRepository.deleteResource(url, userDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
