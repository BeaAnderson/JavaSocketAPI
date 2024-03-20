package com.beacodeart.api.models;

public class Reply {
    private int reply_id;
    private String title;
    private User user;
    private Blog blog;

    public Reply(String title, User user, Blog blog) {
        super();
        this.title = title;
        this.user = user;
        this.blog = blog;
    }

    public int getReply_id() {
        return reply_id;
    }
    public String getTitle() {
        return title;
    }
    public User getUser() {
        return user;
    }
    public Blog getBlog() {
        return blog;
    }
}
