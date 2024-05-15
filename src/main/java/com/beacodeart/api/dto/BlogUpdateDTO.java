package com.beacodeart.api.dto;

public class BlogUpdateDTO {
    private String title;

    public BlogUpdateDTO() {
        super();
    }

    public BlogUpdateDTO(String title) {
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
