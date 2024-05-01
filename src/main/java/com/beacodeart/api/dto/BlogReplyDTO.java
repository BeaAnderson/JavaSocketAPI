package com.beacodeart.api.dto;

public class BlogReplyDTO {
    private int reply_id;
    private String title;
    private BlogUserDTO user;

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

    public BlogUserDTO getUser() {
        return user;
    }

    public void setUser(BlogUserDTO user) {
        this.user = user;
    }
}
