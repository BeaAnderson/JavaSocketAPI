package com.beacodeart.api.DTOs;

public class ReplyDTO2 {
    private int reply_id;
    private String body;
    private String username;

    public int getReply_id() {
        return reply_id;
    }
    public String getBody() {
        return body;
    }
    public String getUsername() {
        return username;
    }
    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
