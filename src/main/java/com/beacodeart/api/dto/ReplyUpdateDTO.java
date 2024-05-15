package com.beacodeart.api.dto;

public class ReplyUpdateDTO {
    private String title;

    public ReplyUpdateDTO() {
        super();
    }

    public ReplyUpdateDTO(String title) {
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
