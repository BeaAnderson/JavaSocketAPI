package com.beacodeart.api.dto;

public class DeleteReplyDTO {
    private String title;

    public DeleteReplyDTO() {
        super();
    }

    public DeleteReplyDTO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
