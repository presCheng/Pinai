package com.jl.domain;

/**
 * User: GunnarXu
 * Desc: 系统消息
 * Date: 2015-01-16
 * Time: 20:17
 * FIXME
 */
public class AcceptNotificationBean {
    private String id, from, nickname, portrait;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
