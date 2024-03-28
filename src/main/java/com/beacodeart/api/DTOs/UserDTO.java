package com.beacodeart.api.DTOs;

public class UserDTO {
    private int user_id;
    private String username;
    private BlogDTO blogDTO;
    private ReplyDTO replyDTO;

    public int getUser_id() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
    public BlogDTO getBlogDTO() {
        return blogDTO;
    }
    public ReplyDTO getReplyDTO() {
        return replyDTO;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setBlogDTO(BlogDTO blogDTO) {
        this.blogDTO = blogDTO;
    }
    public void setReplyDTO(ReplyDTO replyDTO) {
        this.replyDTO = replyDTO;
    }
}
