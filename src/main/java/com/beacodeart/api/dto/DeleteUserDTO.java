package com.beacodeart.api.dto;

public class DeleteUserDTO {
    private String username;

    public DeleteUserDTO() {
        super();
    }

    public DeleteUserDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
