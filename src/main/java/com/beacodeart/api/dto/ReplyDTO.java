package com.beacodeart.api.dto;

public class ReplyDTO {
    private int reply_id;
    private String title;
    private ReplyUserDTO user;
    private ReplyBlogDTO blog;

    public int getReply_id() {
        return reply_id;
    }
    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
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
    public ReplyBlogDTO getBlog() {
        return blog;
    }
    public void setBlog(ReplyBlogDTO blog) {
        this.blog = blog;
    }
}
