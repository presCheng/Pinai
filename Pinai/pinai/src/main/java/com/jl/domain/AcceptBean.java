package com.jl.domain;


import java.io.Serializable;

/**
 * User: GunnarXu
 * Desc: 接收同意消息的bean
 * Date: 2014-12-19
 * Time: 12:59
 * FIXME
 */
public class AcceptBean implements Serializable {
    private String id;//对方id
    private String portrait;//头像地址
    private String nickname;//昵称

    public AcceptBean(String id, String portrait, String nickname) {
        this.id = id;
        this.portrait = portrait;
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
