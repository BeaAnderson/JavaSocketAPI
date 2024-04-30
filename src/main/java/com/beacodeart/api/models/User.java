package com.beacodeart.api.models;

import java.util.List;

public class User {
    private int user_id;
    private String username;
    private String password;
    private List<Blog> blogs;
    private List<Reply> replies;

    public User() {
        super();
    }

    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }
    
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }
    
    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
