package com.beacodeart.api.DTOs;

public class BlogDTO {
    private int blog_id;
    private String title;
    private UserDTO2 UserDTO;
    private ReplyDTO2 replyDTO;

    public int getBlog_id() {
        return blog_id;
    }
    public String getTitle() {
        return title;
    }
    public UserDTO2 getUserDTO() {
        return UserDTO;
    }
    public ReplyDTO2 getReplyDTO() {
        return replyDTO;
    }
}
