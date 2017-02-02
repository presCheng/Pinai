package com.jl.domain;

import java.io.Serializable;

/**
 * 类描述：个人资料
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-4
 * 下午8:35:36
 * 修改备注：
 *
 * @version 1.0.0
 */
public class PersonBean implements Serializable {
    private String sex, bio, nickname, portrait, constellation, tag_str, hobbies, grade, question, self_intro, born_year, school, like;
    private String userLikeMe, answer, userId, verify,points,salary,province,provinceId,salaryId;

    public PersonBean(String sex, String bio, String nickname, String portrait, String constellation, String tag_str, String hobbies, String grade, String question, String self_intro, String born_year, String school, String verify) {

        this.sex = sex;
        this.bio = bio;
        this.nickname = nickname;
        this.portrait = portrait;
        this.constellation = constellation;
        this.tag_str = tag_str;
        this.hobbies = hobbies;
        this.grade = grade;
        this.question = question;
        this.self_intro = self_intro;
        this.born_year = born_year;
        this.school = school;
        this.verify = verify;
    }

    public String getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(String salaryId) {
        this.salaryId = salaryId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return "IndPerson{" +
                "sex='" + sex + '\'' +
                ", bio='" + bio + '\'' +
                ", nickname='" + nickname + '\'' +
                ", portrait='" + portrait + '\'' +
                ", constellation='" + constellation + '\'' +
                ", tag_str='" + tag_str + '\'' +
                ", hobbies='" + hobbies + '\'' +
                ", grade='" + grade + '\'' +
                ", question='" + question + '\'' +
                ", self_intro='" + self_intro + '\'' +
                ", born_year='" + born_year + '\'' +
                ", school='" + school + '\'' +
                '}';
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getTag_str() {
        return tag_str;
    }

    public void setTag_str(String tag_str) {
        this.tag_str = tag_str;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSelf_intro() {
        return self_intro;
    }

    public void setSelf_intro(String self_intro) {
        this.self_intro = self_intro;
    }

    public String getBorn_year() {
        return born_year;
    }

    public void setBorn_year(String born_year) {
        this.born_year = born_year;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {

        this.school = school;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUserLikeMe() {
        return userLikeMe;
    }

    public void setUserLikeMe(String userLikeMe) {
        this.userLikeMe = userLikeMe;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}

