package com.jl.atys.gopin;

/**
 * 类描述：学校类
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24 下午2:24:56
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GoPinSchool {
    private String id;
    private String name;
    private String city;
    private String grade;

    public GoPinSchool() {

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

}
