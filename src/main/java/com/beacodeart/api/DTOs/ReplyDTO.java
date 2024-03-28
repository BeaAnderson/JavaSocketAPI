package com.beacodeart.api.DTOs;

public class ReplyDTO {
    private int reply_id;
    private String title;
    private UserDTO2 userDTO;
    private BlogDTO blogDTO;

    public int getReply_id() {
        return reply_id;
    }
    public String getTitle() {
        return title;
    }
    public UserDTO2 getUserDTO() {
        return userDTO;
    }
    public BlogDTO getBlogDTO() {
        return blogDTO;
    }
    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setUserDTO(UserDTO2 userDTO) {
        this.userDTO = userDTO;
    }
    public void setBlogDTO(BlogDTO blogDTO) {
        this.blogDTO = blogDTO;
    }
}
