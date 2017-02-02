package com.jl.domain;

/**
 * 类名称：GetPostReply
 * 类描述：楼层回复
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-7 上午10:49:43
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetPostReplyBean {
    private String id, content, userportrait, userid, usersex, creatAt, sex;

    public GetPostReplyBean() {
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(String creatAt) {
        this.creatAt = creatAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserportrait() {
        return userportrait;
    }

    public void setUserportrait(String userportrait) {
        this.userportrait = userportrait;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsersex() {
        return usersex;
    }

    public void setUsersex(String usersex) {
        this.usersex = usersex;
    }
}
