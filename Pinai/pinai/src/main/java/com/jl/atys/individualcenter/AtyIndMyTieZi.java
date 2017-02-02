package com.jl.atys.individualcenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jl.atys.dsgy.AtyDsgyComment;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.customs.ConfirmAlertDialog;
import com.jl.domain.MyPostBean;
import com.jl.domain.PostBean;
import com.jl.net.DeleteUserpost;
import com.jl.net.GetUserposts;
import com.jl.utils.UserTools;

import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类描述：我的帖子
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-6
 * 下午2:14:01
 * 修改备注：
 *
 * @version 1.0.0
 */

public class AtyIndMyTieZi extends AtySupport {
    private ImageView portrait;
    private TextView nickname;
    private TextView postCounts;
    private ListView listView;
    private List<PostBean> data;
    private String id;//用户id
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_ind_my_tiezi);
        id = Config.getCacheID(context);
        init();
    }

    private void init() {
        portrait = (ImageView) findViewById(R.id.comment_portrait);
        nickname = (TextView) findViewById(R.id.comment_nickname);
        postCounts = (TextView) findViewById(R.id.comment_post_counts);
        listView = (ListView) findViewById(R.id.comment_list);
        adapter = new Adapter();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String postId = ((PostBean) adapter.getItem(position)).getId();
                Intent intent = new Intent(AtyIndMyTieZi.this, AtyDsgyComment.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
            }
        });
        loadData(id);
    }

    private void loadData(String id) {
        new GetUserposts(id, new GetUserposts.SuccessCallback() {
            @Override
            public void onSuccess(MyPostBean myPostBean) {
                UserTools.displayImage(myPostBean.getPortrait(), portrait, getOptions());
                nickname.setText(myPostBean.getNickname());
                postCounts.setText("帖子数: " + myPostBean.getPostsCount());
                data = myPostBean.getDatas();
                listView.setAdapter(adapter);
                findViewById(R.id.playout).setVisibility(View.GONE);
            }
        }, new GetUserposts.FailCallback() {
            @Override
            public void onFail(String error) {
                findViewById(R.id.playout).setVisibility(View.GONE);
            }
        });
    }

    public void back(View view) {

        finish();
    }

    /**
     * 删除帖子
     *
     * @param postId 帖子id
     */
    private void deleteTheTiezi(final String postId) {
        //退出
        ConfirmAlertDialog.Builder builder = new ConfirmAlertDialog.Builder(
                context);
        builder.setTitle("提示").setContent("确认删除吗？").setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        showProgressDialog(context, "", "删除中...", false);
                        new DeleteUserpost(id, postId, new DeleteUserpost.SuccessCallback() {
                            @Override
                            public void onSuccess() {
                                showToast("删除成功");
                                adapter.notifyDataSetChanged();
                                loadData(id);
                                closeProgressDialog();
                            }
                        }, new DeleteUserpost.FailCallback() {
                            @Override
                            public void onFail(String error) {
                                showToast("出点小问题，请检查下你的网络");
                                closeProgressDialog();
                            }
                        });
                        dialog.cancel();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            final PostBean post = data.get(position);
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(context).inflate(R.layout.row_my_tiezi, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.row_mtiezi_title);
                holder.creatAt = (TextView) convertView.findViewById(R.id.row_mtiezi_time);
                holder.delete = (TextView) convertView.findViewById(R.id.row_mtiezi_delete);
                holder.replyCount = (TextView) convertView.findViewById(R.id.row_mtiezi_msg_shu);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.title.setText(post.getTitle());
            holder.creatAt.setText(post.getCreatedAt());
            holder.replyCount.setText(post.getCommentsCount());
            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteTheTiezi(post.getId());
                }
            });
            return convertView;
        }

        class Holder {
            TextView title;
            TextView creatAt;
            TextView delete;
            TextView replyCount;
        }
    }
}


