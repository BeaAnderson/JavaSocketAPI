package com.beacodeart.api.DTOs;

import java.util.List;

public class BlogDTO {
    private int blog_id;
    private String title;
    private BlogUserDTO userDTO;
    private List<BlogReplyDTO> replyDTOs;

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

    public BlogUserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(BlogUserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public List<BlogReplyDTO> getReplyDTOs() {
        return replyDTOs;
    }

    public void setReplyDTOs(List<BlogReplyDTO> replyDTOs) {
        this.replyDTOs = replyDTOs;
    }

    public void addReply(BlogReplyDTO reply) {
        this.replyDTOs.add(reply);
    }
}
