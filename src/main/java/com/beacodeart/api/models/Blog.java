package com.beacodeart.api.models;

import java.util.List;

public class Blog {
    private int blog_id;
    private String title;
    private User user;
    private List<Reply> replies;

    public Blog(String title, User user, List<Reply> replies) {
        super();
        this.title = title;
        this.user = user;
        this.replies = replies;
    }

    public int getBlog_id() {
        return blog_id;
    }
    public String getTitle() {
        return title;
    }
    public User getUser() {
        return user;
    }
    public List<Reply> getReplies() {
        return replies;
    }
}
