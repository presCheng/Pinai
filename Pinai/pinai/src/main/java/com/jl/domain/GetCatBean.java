package com.jl.domain;

/**
 * 类名称：GetCat
 * 类描述：论坛分类信息对象
 * 创建人：程其春
 * 修改人：程其春
 * 修改时间：2014-11-27 上午10:49:43
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetCatBean {
    private String postId;//帖子id
    private String userId;
    private String title;//标题
    private String nickname;//昵称
    private String sex;//性别
    private String portrait;//头像
    private String comments_count;//评论数
    private String created_at;//创建时间
    private String content; //简要
    private String thumbnails;//图片缩略图

    public GetCatBean(String postId, String title, String nickname,
                      String sex, String portrait, String comments_count,
                      String created_at, String content, String thumbnails) {
        this.postId = postId;
        this.title = title;
        this.nickname = nickname;
        this.sex = sex;
        this.portrait = portrait;
        this.comments_count = comments_count;
        this.created_at = created_at;
        this.content = content;
        this.thumbnails = thumbnails;
    }

    public GetCatBean() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }
}
