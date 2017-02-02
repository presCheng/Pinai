package com.jl.domain;

import java.util.List;

/**
 * 类名称：GetPostChild
 * 类描述：帖子详细信息对象
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-7 上午10:49:43
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetPostChildBean {
    //id帖子id
    //count 回复数
    private String id, userId, nickname, title, userPortrait, userSex, content, count, createdAt;
    private List<GetPostReplyBean> replyList;

    public GetPostChildBean() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String created_at) {
        this.createdAt = created_at;
    }

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<GetPostReplyBean> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<GetPostReplyBean> replyList) {
        this.replyList = replyList;
    }
}

