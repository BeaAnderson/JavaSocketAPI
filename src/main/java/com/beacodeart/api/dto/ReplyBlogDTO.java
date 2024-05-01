package com.beacodeart.api.dto;

public class ReplyBlogDTO {
    private int blog_id;
    private String title;
    private ReplyUserDTO user;

    public int getBlog_id() {
        return blog_id;
    }
    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public ReplyUserDTO getUser() {
        return user;
    }
    public void setUser(ReplyUserDTO user) {
        this.user = user;
    }
}
