package com.jl.atys.dsgy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jl.atys.chat.utils.SmileUtils;
import com.jl.atys.gopin.AtyGoPinProfile;
import com.jl.basic.AtySupport;
import com.jl.domain.GetPostChildBean;
import com.jl.domain.GetPostReplyBean;
import com.jl.utils.UserTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类描述：帖子适配
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24
 * 下午12:42:11
 * 修改备注：
 *
 * @version 1.0.0
 */
public class CommentAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<GetPostChildBean> datas = new ArrayList<GetPostChildBean>();
    private SendMessageCallback sendMessageCallback;
    private Context context;
    private String postId;

    public CommentAdapter(Context context, String postId) {
        this.postId = postId;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void addAll(List<GetPostChildBean> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void clear() {
        datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

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
        GetPostChildBean data = datas.get(position);
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            if (getItemViewType(position) == 0) {
                convertView = mInflater.inflate(R.layout.row_first_tiezi, parent,
                        false);
                holder.portrait = (ImageView) convertView.findViewById(R.id.comment_portrait);
                holder.nickname = (TextView) convertView.findViewById(R.id.comment_nickname);
                holder.creatAt = (TextView) convertView.findViewById(R.id.comment_created_at);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.comment_content);
                holder.title = (TextView) convertView.findViewById(R.id.comment_title);
            } else {
                convertView = mInflater.inflate(R.layout.row_other_tiezi, parent,
                        false);
                holder.portrait = (ImageView) convertView.findViewById(R.id.comment_portrait);
                holder.nickname = (TextView) convertView.findViewById(R.id.comment_nickname);
                holder.creatAt = (TextView) convertView.findViewById(R.id.comment_created_at);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.comment_content);
                holder.reply = (TextView) convertView.findViewById(R.id.comment_reply);
            }
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        setItemData(data, holder, convertView, position);
        return convertView;
    }

    /**
     * 设置item数据
     *
     * @param data
     * @param holder
     */
    private void setItemData(final GetPostChildBean data, Holder holder, View convertView, final int position) {
        if (data.getUserSex().equals("F")) {
            holder.nickname.setTextColor(context.getResources().getColor(R.color.background_pink));
        } else {
            holder.nickname.setTextColor(context.getResources().getColor(R.color.blue));
        }

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
        //只有一楼有题目
        if (position == 0) {
            holder.title.setText(data.getTitle());
        }
        //回复  这个一楼没有所以要做个判断
        if (holder.reply != null) {
            holder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sendMessageCallback != null) {

                        sendMessageCallback.sendMessage(data.getId(), data.getUserId(), getItemId(position), data.getNickname());
                    }
                }
            });
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
                //System.err.println(content);
                //System.err.println(node.toString());
                TextView t = new TextView(context);
                t.setTextColor(context.getResources().getColor(R.color.gray_normal));
                t.setText(SmileUtils.getSmiledText(context, content.trim(), 32));
                holder.layout.addView(t);
                //System.out.println(node.toString());
            }
        }
        //一层楼下的回复
        final List<GetPostReplyBean> re = data.getReplyList();
        if (re != null) {
            for (int i = 0; i < re.size(); i++) {
                GetPostReplyBean gr = re.get(i);
                TextView c = new TextView(context);
                //String content = gr.getContent().replace("\\n", "\n");
                //c.setText(getTwoColorText(gr.getContent()));
                setTextLine(c, gr.getContent(), gr.getUserid(), data.getId(), data.getUserId(), getItemId(position));
                TextView t = new TextView(context);
                t.setTextSize(12);
                t.setText(gr.getCreatAt());
                t.setTextColor(context.getResources().getColor(R.color.gray_normal));
                holder.layout.addView(c);
                holder.layout.addView(t);
                LinearLayout l = new LinearLayout(context);
                l.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                l.setBackgroundResource(R.color.gray);
                holder.layout.addView(l);
            }
            //两条之后就显示更多
            if (re.size() >= 3) {
                TextView t = new TextView(context);
                t.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                t.setText("点击查看更多...");
                t.setTextSize(14);
                t.setTextColor(R.drawable.btn_blue_normal_shape);
                t.setGravity(Gravity.CENTER);
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, AtyDsgyMoreReply.class).putExtra("postid", postId).putExtra("commentid", data.getId());
                        ((AtySupport) context).startActivityForResult(i, 0);
                    }
                });
                holder.layout.addView(t);
            }
        }
    }
    private void setTextLine(TextView textView,String str, final String cId, final String id, final String userId, final long position){
        try {
            int length = str.indexOf(":");
            String tt = str.substring(0, length);
            String dd = str.substring(length, str.length()).replace("\\n", "\n");
            if (tt.contains(" 回复 ")) {
                final String[] names = tt.split(" 回复 ");
                SpannableString spStr = new SpannableString(names[0]);
                spStr.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(context.getResources().getColor(R.color.background_pink));       //设置文件颜色
                        ds.setUnderlineText(true);      //设置下划线
                    }
                    @Override
                    public void onClick(View widget) {
                        Intent i = new Intent(context, AtyGoPinProfile.class);
                        String receiverId = cId;
                        i.putExtra("receiverId", receiverId);
                        context.startActivity(i);
                    }
                }, 0, names[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.append(spStr);
                textView.append(" 回复 ");
                textView.append(names[1]+dd);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMessageCallback.sendMessage(id, userId, position, names[0]);
                    }
                });
            } else {
                SpannableString spStr = new SpannableString(tt);
                spStr.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(context.getResources().getColor(R.color.background_pink));       //设置文件颜色
                        ds.setUnderlineText(true);      //设置下划线
                    }
                    @Override
                    public void onClick(View widget) {
                        Intent i = new Intent(context, AtyGoPinProfile.class);
                        String receiverId = cId;
                        i.putExtra("receiverId", receiverId);
                        context.startActivity(i);
                    }
                }, 0, tt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.append(spStr);
                textView.append(dd);
            }
            textView.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
            textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
            textView.setTextSize(14);
            textView.setTextColor(context.getResources().getColor(R.color.gray_normal));
        }catch (Exception e){
            textView.setText(str);
        }
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

    public DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_photo)           //加载图片时的图片
                .showImageForEmptyUri(R.drawable.empty_photo)         //没有图片资源时的默认图片
                .showImageOnFail(R.drawable.empty_photo)              //加载失败时的图片
                .cacheInMemory(true)                                  //启用内存缓存
                .cacheOnDisk(true)                                    //启用外存缓存
                .considerExifParams(true)                             //启用EXIF和JPEG图像格式
                .build();
        return options;
    }
    public void setSendMessageCallback(SendMessageCallback sendMessageCallback) {
        this.sendMessageCallback = sendMessageCallback;
    }


    public interface SendMessageCallback {
        /**
         * @param commentid
         * @param reply_id
         * @param floor
         */
        void sendMessage(String commentid, String reply_id, long floor, String nickname);
    }

    class Holder {
        ImageView portrait;
        TextView nickname;
        TextView creatAt;
        TextView title;
        LinearLayout layout;
        TextView reply;
    }
}
