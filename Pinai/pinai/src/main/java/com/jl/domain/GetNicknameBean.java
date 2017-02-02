package com.jl.domain;

/**
 * User: GunnarXu
 * Desc: id与nickname
 * Date: 2014-12-15
 * Time: 13:45
 * FIXME
 */
public class GetNicknameBean {
    private String id;
    private String nickName;
    private String portrait;

    public GetNicknameBean(String id, String nickName, String portrait) {
        this.id = id;
        this.nickName = nickName;
        this.portrait = portrait;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
