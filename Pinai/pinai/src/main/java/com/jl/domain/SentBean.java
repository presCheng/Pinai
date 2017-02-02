package com.jl.domain;

/**
 * 类名称：ChaseMe
 * 类描述：追我的人数据
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-10 下午1:36:16
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SentBean {
    private String portrait;
    private String sex;
    private String status;// 追求的状态
    private String created_at;
    private String count;
    private String id;// 对方id
    private String name;// 对方昵称
    private String school;// 对方学校

    public SentBean() {
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {

        this.sex = sex;
    }
}
