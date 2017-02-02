package com.jl.domain;

import java.util.List;

/**
 * User: GunnarXu
 * Desc: 我的帖子
 * Date: 2015-01-11
 * Time: 19:47
 * FIXME
 */
public class MyPostBean {
    String portrait, nickname, postsCount;
    List<PostBean> datas;

    public MyPostBean() {
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

    public String getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(String postsCount) {
        this.postsCount = postsCount;
    }

    public List<PostBean> getDatas() {
        return datas;
    }

    public void setDatas(List<PostBean> datas) {
        this.datas = datas;
    }
}

