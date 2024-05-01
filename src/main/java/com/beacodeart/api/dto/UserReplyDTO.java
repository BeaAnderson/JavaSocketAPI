package com.beacodeart.api.dto;

public class UserReplyDTO {
    private int id;
    private String title;
    private UserBlogDTO blogDTO;
    
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public UserBlogDTO getBlogDTO() {
        return blogDTO;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBlogDTO(UserBlogDTO blogDTO) {
        this.blogDTO = blogDTO;
    }
}
