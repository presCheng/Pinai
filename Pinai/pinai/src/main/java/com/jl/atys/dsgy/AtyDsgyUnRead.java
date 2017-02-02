package com.jl.atys.dsgy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jl.basic.AtyListSupport;
import com.jl.basic.Config;
import com.jl.dao.UnReadDao;
import com.jl.domain.UnReadBean;
import com.jl.net.GetNotifications;
import com.jl.utils.UserTools;

import java.util.List;

import hrb.jl.pinai.R;

public class AtyDsgyUnRead extends AtyListSupport{
    private List<UnReadBean> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_dsgy_un_read);
        getUnRead();
    }

    private void getUnRead() {
        new GetNotifications(Config.getCacheID(context), "20", "20", new GetNotifications.SuccessCallback() {
            @Override
            public void onSuccess(List<UnReadBean> datas) {
                //把网络上更新的存入本地
                UnReadDao unReadDao = new UnReadDao(context);
                unReadDao.saveUnReadList(datas);
                AtyDsgyUnRead.this.datas = unReadDao.getUnReadList();
                Adpter adpter = new Adpter();
                setListAdapter(adpter);
                findViewById(R.id.playout).setVisibility(View.GONE);
            }
        }, new GetNotifications.FailCallback() {
            @Override
            public void onFail(String error) {
                //直接读取本地数据
                UnReadDao unReadDao = new UnReadDao(context);
                unReadDao.saveUnReadList(datas);
                AtyDsgyUnRead.this.datas = unReadDao.getUnReadList();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 保证只有一个界面
        super.onNewIntent(intent);
        //setIntent(intent);  更新数据
        getUnRead();

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        UnReadBean u = datas.get(position);
        if (u.getCategory().equals("6")) {
            //6(别人回复帖子)
            Intent i = new Intent(AtyDsgyUnRead.this, AtyDsgyComment.class);
            i.putExtra("postId", u.getPostId());
            startActivity(i);
        } else {
            //7(别人回复评论)
            Intent i = new Intent(AtyDsgyUnRead.this, AtyDsgyUnReadContent.class);
            i.putExtra("postid", u.getPostId());
            i.putExtra("commentid", u.getCommentId());
            startActivity(i);
        }

    }

    public void back(View v) {
        finish();
    }

    class Adpter extends BaseAdapter {
        @Override
        public int getCount() {
            return datas.size();
        }


        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final UnReadBean unReadBean = datas.get(position);
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(context).inflate(R.layout.row_un_read, parent, false);
                holder.protrait = (ImageView) convertView.findViewById(R.id.comment_portrait);
                holder.nickname = (TextView) convertView.findViewById(R.id.comment_nickname);
                holder.creatAt = (TextView) convertView.findViewById(R.id.comment_created_at);
                holder.reply = (TextView) convertView.findViewById(R.id.comment_reply);
                holder.oreply = (TextView) convertView.findViewById(R.id.comment_original_reply);
                holder.category = (TextView) convertView.findViewById(R.id.comment_category);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            UserTools.displayImage(unReadBean.getPortrait(), holder.protrait, AtyDsgyUnRead.this.getOptions());
            holder.nickname.setText(unReadBean.getNickname());
            holder.creatAt.setText(unReadBean.getCreatedAt());
            holder.reply.setText(unReadBean.getOriginalContent());
            holder.oreply.setText(unReadBean.getContent());
            if (unReadBean.getCategoryId().equals("1")) {
                holder.category.setText("爱诊所");
            } else if (unReadBean.getCategoryId().equals("2")) {
                holder.category.setText("男人帮");
            } else if (unReadBean.getCategoryId().equals("3")) {
                holder.category.setText("女人窝");
            }
            return convertView;
        }

        class Holder {
            ImageView protrait;
            TextView nickname;
            TextView creatAt;
            TextView reply;
            TextView oreply;
            TextView category;
        }
    }

}
