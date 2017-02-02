package com.jl.domain;

/**
 * User: GunnarXu
 * Desc: 系统消息
 * Date: 2015-01-16
 * Time: 20:17
 * FIXME
 */
public class SystemNotificationBean {
    private String id, createdAt, content, status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
