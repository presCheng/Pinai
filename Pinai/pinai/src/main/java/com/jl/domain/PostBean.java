package com.jl.domain;

/**
 * User: GunnarXu
 * Desc:
 * Date: 2015-01-11
 * Time: 19:51
 * FIXME
 */

public class PostBean {
    String id, title, createdAt, commentsCount;

    public PostBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }
}