package com.jl.atys.dsgy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jl.atys.chat.utils.SmileUtils;
import com.jl.basic.AtySupport;
import com.jl.domain.GetCatBean;
import com.jl.utils.UserTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类描述：帖子主页适配器
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24
 * 下午12:42:11
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ForumAdapter extends BaseAdapter {
    private Context context;
    private List<Object> data = new ArrayList<Object>();
    private LayoutInflater inflater;

    public ForumAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addAll(List<Object> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

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
    public int getItemViewType(int position) {
        if (data.get(position) instanceof List && position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            //如果有置顶的话，并且是一楼.
            if (getItemViewType(position) == 0) {
                convertView = inflater.inflate(R.layout.bbs_top_list, null);
                LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.top_layout);
                List<GetCatBean> getcats = (List<GetCatBean>) data.get(0);
                for (int i = 0; i < getcats.size(); i++) {
                    final GetCatBean gc = getcats.get(i);
                    View view = inflater.inflate(R.layout.top_item, null);
                    TextView topTitle = (TextView) view.findViewById(R.id.top_title);
                    TextView topCount = (TextView) view.findViewById(R.id.top_count);
                    topCount.setText(gc.getComments_count());
                    String title =gc.getTitle();
                    if (title.length()>12){
                        title = title.substring(0,12)+"...";
                    }
                    topTitle.setText(title);
                    TextView topContent = (TextView) view.findViewById(R.id.content);
                    topContent.setText(gc.getContent());

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postId = gc.getPostId();
                            Intent intent = new Intent(context, AtyDsgyComment.class);
                            intent.putExtra("postId", postId);
                            context.startActivity(intent);
                        }
                    });
                    layout.addView(view);
                    LinearLayout line = new LinearLayout(context);
                    line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
                    line.setBackgroundColor(context.getResources().getColor(R.color.gray));
                    layout.addView(line);
                }
                //删除最后一个view（线）
                layout.removeViewAt(layout.getChildCount() - 1);
            } else {
                convertView = inflater.inflate(R.layout.bbs_list, null);
                holder.nickname = (TextView) convertView.findViewById(R.id.bbs_nickname);
                holder.title = (TextView) convertView.findViewById(R.id.bbs_title);
                holder.createdAt = (TextView) convertView.findViewById(R.id.bbs_created_at);
                holder.summary = (TextView) convertView.findViewById(R.id.bbs_content);
                holder.portrait = (ImageView) convertView.findViewById(R.id.bbs_portrait);
                holder.sex = (ImageView) convertView.findViewById(R.id.bbs_sex);
                holder.commentsCount = (TextView) convertView.findViewById(R.id.bbs_comments_count);
                holder.iOne = (ImageView) convertView.findViewById(R.id.bbs_iv_1);
                holder.iTwo = (ImageView) convertView.findViewById(R.id.bbs_iv_2);
                holder.iThree = (ImageView) convertView.findViewById(R.id.bbs_iv_3);
                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //普通帖子
        if (data.get(position) instanceof GetCatBean) {
            GetCatBean info = (GetCatBean) data.get(position);
            String title =info.getTitle();
            if (title.length()>12){
                title = title.substring(0,12)+"...";
            }
            holder.title.setText(title);
            holder.nickname.setText(info.getNickname());
            if (info.getSex().equals("F")) {
                holder.sex.setBackgroundResource(R.drawable.big_girl);
                holder.nickname.setTextColor(context.getResources().getColor(R.color.background_pink));
            } else {
                holder.sex.setBackgroundResource(R.drawable.big_boy);
                holder.nickname.setTextColor(context.getResources().getColor(R.color.blue));
            }
            AtySupport atySupport = (AtySupport) context;
            UserTools.displayImage(info.getPortrait(), holder.portrait, atySupport.getOptions());
            holder.commentsCount.setText(info.getComments_count());
            holder.createdAt.setText(info.getCreated_at());
            // String filename = "[:18]";
            // holder.summary.setText(SmileUtils.getSmiledText(context,filename , 26));
            //文字替换成表情
            holder.summary.setText(SmileUtils.getSmiledText(context, info.getContent(), 50));
            //缓存机制···闹听
            holder.iOne.setVisibility(View.GONE);
            holder.iTwo.setVisibility(View.GONE);
            holder.iThree.setVisibility(View.GONE);
            try {
                String[] url = info.getThumbnails().split(",");
                holder.iOne.setScaleType(ImageView.ScaleType.CENTER );
                holder.iTwo.setScaleType(ImageView.ScaleType.CENTER );
                holder.iThree.setScaleType(ImageView.ScaleType.CENTER  );
                for (int i = 0; i < url.length; i++) {
                    if (i == 0 && !url[i].equals("")) {
                        holder.iOne.setVisibility(View.VISIBLE);
                        holder.iTwo.setVisibility(View.INVISIBLE);
                        holder.iThree.setVisibility(View.INVISIBLE);
                        UserTools.displayImage(url[i], holder.iOne, getOptions());
                    } else if (i == 1 && !url[i].equals("")) {
                        holder.iTwo.setVisibility(View.VISIBLE);
                        UserTools.displayImage(url[i], holder.iTwo, getOptions());
                    } else if (i == 2 && !url[i].equals("")) {
                        holder.iThree.setVisibility(View.VISIBLE);
                        UserTools.displayImage(url[i], holder.iThree, getOptions());
                    }
                }
            } catch (Exception e) {
                Log.e("e", "解析失败");
            }
        }
        return convertView;
    }

    public DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_photo)           //加载图片时的图片
                .showImageForEmptyUri(R.drawable.empty_photo)         //没有图片资源时的默认图片
                .showImageOnFail(R.drawable.empty_photo)              //加载失败时的图片
                .cacheInMemory(false)                                  //启用内存缓存
                .cacheOnDisk(false)                                    //启用外存缓存
                .considerExifParams(true)                             //启用EXIF和JPEG图像格式
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
        return options;
    }

    public class ViewHolder {
        private ImageView portrait;
        private ImageView sex;
        private TextView nickname, createdAt, summary, title, commentsCount;
        private ImageView iOne, iTwo, iThree;
    }
}
