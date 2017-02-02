package com.jl.atys.individualcenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jl.atys.chat.utils.SmileUtils;
import com.jl.utils.UserTools;

import java.text.SimpleDateFormat;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * User: GunnarXu
 * Desc:
 * Date: 2014-12-30
 * Time: 12:42
 * FIXME
 */
public class FeedAdpter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<FeedBack> mDatas;
    private Context context;

    public FeedAdpter(Context context, List<FeedBack> mDatas) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        FeedBack feedBack = mDatas.get(position);
        if (feedBack.getType() == FeedBack.Type.INCOMEING) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FeedBack feedBack = mDatas.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            //通过itemtype设置布局
            if (getItemViewType(position) == 0) {
                convertView = mInflater.inflate(R.layout.row_received_message, parent,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.mPhoto = (ImageView) convertView.findViewById(R.id.iv_userhead);
                viewHolder.mMsg = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                viewHolder.mDate = (TextView) convertView.findViewById(R.id.timestamp);
            } else {
                convertView = mInflater.inflate(R.layout.row_sent_message, parent,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.mPhoto = (ImageView) convertView.findViewById(R.id.iv_userhead);
                viewHolder.mMsg = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                viewHolder.mDate = (TextView) convertView.findViewById(R.id.timestamp);
                ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
                pb.setVisibility(View.GONE);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.mDate.setText(df.format(feedBack.getDate()));
        viewHolder.mMsg.setText(SmileUtils.getSmiledText(context, feedBack.getMsg(), 30),
                TextView.BufferType.SPANNABLE);
        if (!TextUtils.isEmpty(feedBack.getPortrait())) {
            UserTools.displayImage(feedBack.getPortrait(), viewHolder.mPhoto, null);
        }
        return convertView;
    }

    private final class ViewHolder {
        ImageView mPhoto;
        TextView mDate;
        TextView mMsg;
        ImageView avator;
    }

}
