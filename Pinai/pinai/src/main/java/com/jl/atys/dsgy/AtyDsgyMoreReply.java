package com.jl.atys.dsgy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jl.atys.chat.utils.SmileUtils;
import com.jl.atys.chat.widget.PasteEditText;
import com.jl.atys.gopin.AtyGoPinProfile;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.domain.GetPostChildBean;
import com.jl.domain.GetPostReplyBean;
import com.jl.net.ForumGetReply;
import com.jl.net.ForumPostcomment;
import com.jl.utils.UserTools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.util.List;

import hrb.jl.pinai.R;

/**
 * 更多评论界面
 */
public class AtyDsgyMoreReply extends AtySupport {
    private Holder holder;
    private PasteEditText sendMessage;
    private String postId;
    /**
     * 设置item数据
     *
     * @param data
     * @param holder
     */
    private String commentID;
    private String replyID;
    private String louNikeName;//楼层昵称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_dsgy_more_reply);
        postId = (String) getIntent().getExtras().get("postid");
        String replyId = (String) getIntent().getExtras().get("commentid");
        holder = new Holder();
        holder.portrait = (ImageView) findViewById(R.id.comment_portrait);
        holder.nickname = (TextView) findViewById(R.id.comment_nickname);
        holder.creatAt = (TextView) findViewById(R.id.comment_created_at);
        holder.sex = (ImageView) findViewById(R.id.comment_sex);
        holder.layout = (LinearLayout) findViewById(R.id.comment_content);
        sendMessage = (PasteEditText) findViewById(R.id.comment_sendmessage);
        new ForumGetReply(postId, replyId, new ForumGetReply.SuccessCallback() {
            @Override
            public void onSuccess(GetPostChildBean gc) {
                setItemData(gc, holder);
                findViewById(R.id.playout).setVisibility(View.GONE);
            }
        }, new ForumGetReply.FailCallback() {
            @Override
            public void onFail(String error) {
                findViewById(R.id.pbar).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.ptext)).setText("评论已经被删除或不存在 TAT...");
                findViewById(R.id.comment_bar_bottom).setVisibility(View.GONE);//把回复框去掉
            }
        });
    }

    private void setItemData(final GetPostChildBean data, Holder holder) {
        commentID = data.getId();
        replyID = data.getUserId();
        louNikeName = data.getNickname();
        //头像
        AtySupport atySupport = (AtySupport) context;
        UserTools.displayImage(data.getUserPortrait(), holder.portrait, atySupport.getOptions());
        //点击头像跳转到个人信息追Ta界面
        holder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AtyGoPinProfile.class);
                String receiverId = data.getUserId();
                i.putExtra("receiverId", receiverId);
                context.startActivity(i);
            }
        });
        //昵称
        holder.nickname.setText(data.getNickname());
        //时间
        holder.creatAt.setText(data.getCreatedAt());
        if (data.getUserSex().equals("F")) {
            holder.sex.setBackgroundResource(R.drawable.big_girl);
        } else {
            holder.sex.setBackgroundResource(R.drawable.big_boy);
        }
        holder.layout.removeAllViews();
        //解析HTML字符串返回一个Document实现
        Document doc = Jsoup.parse(data.getContent());
        List<Node> nodes = doc.body().childNodes();
        for (Node node : nodes) {
            if (node.hasAttr("src")) {
                //图片和表情转换
                final String url = node.attr("src");
                //自定义表情显示
                if (containsStr(url, "http://img.baidu.com/")) {
                    WebView webw = new WebView(context);
                    webw.loadUrl(url);
                    WebSettings webSettings = webw.getSettings(); // webView: 类WebView的实例
                    webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);  //就是这句
                    holder.layout.addView(webw);
                } else {
                    //图片显示
                    ImageView imageView = new ImageView(context);
                    imageView.setPadding(3, 3, 3, 3);
                    //这里设置大小
                    UserTools.displayImageHasListener(url, imageView, getOptions());
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, AtyShowBigImage.class).putExtra("name", url));
                        }
                    });
                    holder.layout.addView(imageView);
                }
            } else {
                //文字内容
                String content = node.toString();
                content = content.replace("\\n", "\n");
                TextView t = new TextView(context);
                t.setTextColor(context.getResources().getColor(R.color.gray_normal));
                t.setText(SmileUtils.getSmiledText(context, content, 24));
                holder.layout.addView(t);
            }
        }
        //一层楼下的回复
        final List<GetPostReplyBean> re = data.getReplyList();
        if (re != null) {
            for (int i = 0; i < re.size(); i++) {
                GetPostReplyBean gr = re.get(i);
                setReply(holder, gr);
            }
        }
    }

    private void setReply(Holder holder, GetPostReplyBean gr) {
        TextView c = new TextView(context);
        String content = gr.getContent().replace("\\n", "\n");
        c.setText(getTwoColorText(content));
        c.setTextSize(14);
        c.setTextColor(context.getResources().getColor(R.color.gray_normal));
        TextView t = new TextView(context);
        t.setTextSize(14);
        t.setText(gr.getCreatAt());
        t.setTextColor(context.getResources().getColor(R.color.gray_normal));
        holder.layout.addView(c);
        holder.layout.addView(t);
        LinearLayout l = new LinearLayout(context);
        l.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        l.setBackgroundResource(R.color.gray);
        holder.layout.addView(l);
    }

    public void send(View v) {
        if (TextUtils.isEmpty(sendMessage.getText().toString())) {
            showToast("不能发送空消息，欧巴");
            return;
        }
        submitMsgReply();
    }

    /**
     * 回复评论
     */
    private void submitMsgReply() {
        showProgressDialog(context, "", "回复中...", false);
        sendMessage.setTag(Config.getCacheNickName(context) + " 回复 " + louNikeName + ":");
        String content = sendMessage.getTag() + sendMessage.getText().toString();
        sendMessage.setText("");
        String type = ForumPostcomment.TYPE_REPLY;
        String userId = Config.getCacheID(context);
        //设置回复帖子
        new ForumPostcomment(type, content, userId, postId, replyID, commentID, new ForumPostcomment.SuccessCallback() {
            @Override
            public void onSuccess() {
                AtyDsgyMoreReply.this.showToast("回复成功");
                Intent i = new Intent(AtyDsgyMoreReply.this, AtyDsgyComment.class);
                setResult(AtyDsgyComment.RESULT_MORE_REPLY, i);
                finish();
                closeProgressDialog();
            }
        }, new ForumPostcomment.FailCallback() {
            @Override
            public void onFail(String error) {
                AtyDsgyMoreReply.this.showToast("回复失败");
                closeProgressDialog();
            }
        });
    }

    private SpannableStringBuilder getTwoColorText(String str1) {
        SpannableStringBuilder style = new SpannableStringBuilder(str1);
        int length = str1.indexOf(":");
        if (length != -1) {
            style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.background_pink)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return style;
    }

    /**
     * 字符串包含
     *
     * @param str 原字符串
     * @param reg 匹配字符串
     * @return
     */
    public boolean containsStr(String str, String reg) {
        return str.contains(reg);
    }

    public void back(View v) {
        finish();
    }

    private class Holder {
        ImageView portrait;
        ImageView sex;
        TextView nickname;
        TextView creatAt;
        LinearLayout layout;
    }
}
