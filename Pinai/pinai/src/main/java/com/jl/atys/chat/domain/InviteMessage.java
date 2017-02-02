/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jl.atys.chat.domain;

import java.io.Serializable;

public class InviteMessage implements Serializable {
    private int id;//消息id
    private String from;//来自的哪个的id
    //时间
    private long time;
    //头像
    private String portrait;
    //添加理由
    private String reason;
    //未验证，已同意等状态
    private InviteMesageStatus status;
    //好友昵称
    private String nickname;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InviteMesageStatus getStatus() {
        return status;
    }

    public void setStatus(InviteMesageStatus status) {
        this.status = status;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public enum InviteMesageStatus {
        /**
         * 对方申请
         */
        BEAPPLYED,
        /**
         * 我同意了对方的请求
         */
        AGREED,
        /**
         * 我拒绝了对方的请求
         */
        REFUSED

    }

}



