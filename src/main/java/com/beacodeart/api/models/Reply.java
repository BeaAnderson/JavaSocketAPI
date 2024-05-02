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

    public Reply() {
		//TODO Auto-generated constructor stub
	}

	public int getReply_id() {
        return reply_id;
    }
    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Blog getBlog() {
        return blog;
    }
    public void setBlog(Blog blog) {
        this.blog = blog;
    }
}
