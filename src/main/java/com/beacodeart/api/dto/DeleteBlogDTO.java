package com.beacodeart.api.dto;

public class DeleteBlogDTO {
    private String title;

    public DeleteBlogDTO() {
        super();
    }

    public DeleteBlogDTO(String title) {
        super();
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
