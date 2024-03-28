package com.beacodeart.api.DTOs;

import java.util.List;

public class UserDTO {
    private int id;
    private String username;
    private List<UserBlogDTO> blogs;
    private List<UserReplyDTO> replies;

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
    public void setBlogs(List<UserBlogDTO> blogs) {
        this.blogs = blogs;
    }
    public void setReplies(List<UserReplyDTO> replies) {
        this.replies = replies;
    }
}
