/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jl.atys.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.jl.atys.chat.utils.SmileUtils;
import com.jl.atys.sms.AtyChatAll;
import com.jl.basic.Config;
import com.jl.basic.Constant;
import com.jl.domain.EMConversationBean;
import com.jl.utils.UserTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 显示所有聊天记录adpater
 */
public class ChatAllHistoryAdapter extends ArrayAdapter<EMConversationBean> {

    private LayoutInflater inflater;
    private List<EMConversationBean> conversationList;
    private List<EMConversationBean> copyConversationList;
    private ConversationFilter conversationFilter;
    private Context context;

    public ChatAllHistoryAdapter(Context context, int textViewResourceId, List<EMConversationBean> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.conversationList = objects;
        copyConversationList = new ArrayList<>();
        copyConversationList.addAll(objects);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return super.getCount() + 2;
    }

    @Override
    public EMConversationBean getItem(int position) {
        return super.getItem(position - 2);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (position == 0) {
            convertView = inflater.inflate(R.layout.row_chat_top, parent, false);
            TextView unreadMsgView = (TextView) convertView.findViewById(R.id.unread_msg_number);
            if (Config.getCacheNewFriendUnRead(context) > 0) {
                unreadMsgView.setVisibility(View.VISIBLE);
                unreadMsgView.setText(Config.getCacheNewFriendUnRead(context) + "");
            } else {
                unreadMsgView.setVisibility(View.INVISIBLE);
            }
            return convertView;
        } else if (position == 1) {
            convertView = inflater.inflate(R.layout.row_chat_top, parent, false);
            ((TextView) convertView.findViewById(R.id.name)).setText("面试列表");
            convertView.findViewById(R.id.avatar).setBackgroundResource(R.drawable.sms_ms);
            return convertView;
        } else {
            //if(convertView==null) {
                convertView = inflater.inflate(R.layout.row_chat_history, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
                holder.message = (TextView) convertView.findViewById(R.id.message);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                holder.msgState = convertView.findViewById(R.id.msg_state);
                holder.list_item_layout = (RelativeLayout) convertView.findViewById(R.id.list_item_layout);
                //holder = (ViewHolder) convertView.getTag();
           // }

            // 获取与此用户的会话
            EMConversationBean conversation = getItem(position);
            // 获取用户username
            String username = conversation.getName();
            // 本地或者服务器获取用户详情，以用来显示头像和nick
            AtyChatAll c = (AtyChatAll) context;
            UserTools.displayImage(conversation.getPortrait(), holder.avatar, c.getOptions());
            try {
                if (username.equals(Constant.NEW_FRIEND_USERNAME)) {
                    holder.name.setText("申请与通知");
                }
                holder.name.setText(username);
            } catch (Exception e) {
                //名字获取异常
            }
            if (conversation.getUnreadMsgCount() > 0) {
                // 显示与此用户的消息未读数
                holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
                holder.unreadLabel.setVisibility(View.VISIBLE);
            } else {
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            }
            if (conversation.getMsgCount() != 0) {
                // 把最后一条消息的内容作为item的message内容
                EMMessage lastMessage = conversation.getLastMessage();
                holder.message.setText(SmileUtils.getSmiledText(getContext(), getMessageDigest(lastMessage, (this.getContext())), 20),
                        BufferType.SPANNABLE);
                holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
                if (lastMessage.direct == EMMessage.Direct.SEND && lastMessage.status == EMMessage.Status.FAIL) {
                    holder.msgState.setVisibility(View.VISIBLE);
                } else {
                    holder.msgState.setVisibility(View.GONE);
                }
            }
            return convertView;
        }
    }

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    private String getMessageDigest(EMMessage message, Context context) {
        String digest;
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    // 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_recv");
                    digest = getStrng(context, R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_prefix");
                    digest = getStrng(context, R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                digest = getStrng(context, R.string.picture) + imageBody.getFileName();
                break;
            case VOICE:// 语音消息
                digest = getStrng(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getStrng(context, R.string.video);
                break;

            case TXT: // 文本消息
                if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                } else {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
                }
                break;
            case FILE: // 普通文件消息
                digest = getStrng(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }

    String getStrng(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(conversationList);
        }
        return conversationFilter;
    }

    private static class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        TextView name;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 用户头像
         */
        ImageView avatar;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */
        RelativeLayout list_item_layout;

    }

    private class ConversationFilter extends Filter {
        List<EMConversationBean> mOriginalValues = null;

        public ConversationFilter(List<EMConversationBean> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversationBean>();
            }
            if (prefix == null || prefix.length() == 0) {
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                String prefixString = prefix.toString();
                final int count = mOriginalValues.size();
                final ArrayList<EMConversationBean> newValues = new ArrayList<EMConversationBean>();

                for (int i = 0; i < count; i++) {
                    final EMConversationBean value = mOriginalValues.get(i);
                    String username = value.getUserName();

                    EMGroup group = EMGroupManager.getInstance().getGroup(username);
                    if (group != null) {
                        username = group.getGroupName();
                    }

                    // First match against the whole ,non-splitted value
                    if (username.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = username.split(" ");
                        // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            conversationList.clear();
            conversationList.addAll((List<EMConversationBean>) results.values);
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }

    }
}
