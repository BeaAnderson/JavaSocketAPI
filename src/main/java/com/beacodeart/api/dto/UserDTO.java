package com.beacodeart.api.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private int id;
    private String username;
    private String password;
    private ArrayList<UserBlogDTO> blogs;
    private ArrayList<UserReplyDTO> replies;

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public List<UserBlogDTO> getBlogs() {
        return blogs;
    }
    public List<UserReplyDTO> getReplies() {
        return replies;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setBlogs(ArrayList<UserBlogDTO> blogs) {
        this.blogs = blogs;
    }
    public void setReplies(ArrayList<UserReplyDTO> replies) {
        this.replies = replies;
    }
    public void addBlog(UserBlogDTO userBlogDTO){
        this.blogs.add(userBlogDTO);
    }
    public void addReply(UserReplyDTO reply) {
        this.replies.add(reply);
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
