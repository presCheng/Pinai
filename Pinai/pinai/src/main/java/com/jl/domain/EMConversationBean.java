package com.jl.domain;

import com.easemob.chat.EMMessage;

/**
 * User: GunnarXu
 * Desc:
 * Date: 2014-12-19
 * Time: 14:00
 * FIXME
 */
public class EMConversationBean {
    private String name;
    private String userName;
    private boolean isGroup;
    private EMMessage lastMessage;
    private int unreadMsgCount;
    private int msgCount;
    private String portrait;

    public EMConversationBean() {
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public EMMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(EMMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
