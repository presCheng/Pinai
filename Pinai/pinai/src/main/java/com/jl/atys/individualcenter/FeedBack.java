package com.jl.atys.individualcenter;

import java.util.Date;

/**
 * User: GunnarXu
 * Desc:
 * Date: 2014-12-30
 * Time: 12:45
 * FIXME
 */
public class FeedBack {
    private String name;
    private String msg;
    private Type type;
    private Date date;
    private String portrait;


    public FeedBack() {
    }

    public FeedBack(String name, String msg, Type type, Date date, String portrait) {
        this.name = name;
        this.msg = msg;
        this.type = type;
        this.date = date;
        this.portrait = portrait;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public enum Type {
        INCOMEING, OUTCOMEING
    }
}
