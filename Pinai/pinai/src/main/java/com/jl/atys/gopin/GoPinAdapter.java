package com.jl.atys.gopin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jl.basic.AtySupport;
import com.jl.db.SqliteUtils;
import com.jl.utils.UserTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hrb.jl.pinai.R;

/**
 * 类描述：去招聘listview适配器
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-23
 * 下午12:30:09
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GoPinAdapter extends BaseAdapter {

    private Context context = null;
    private List<GoPinData> data = new ArrayList<>();
    private int [] bqs;
    private int []pazss;
    public GoPinAdapter(Context context) {
        this.context = context;
        bqs=new int []{R.drawable.bq_1, R.drawable.bq_2, R.drawable.bq_3, R.drawable.bq_4};
        pazss=new int []{R.drawable.pazs_1, R.drawable.pazs_2, R.drawable.pazs_3, R.drawable.pazs_4, R.drawable.pazs_5};
    }

    public Context getContext() {
        return context;
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
        return Integer.parseInt(data.get(position).getId());
    }

    public void addAll(List<GoPinData> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_gopin_cell, null);
            holder = new Holder();
            holder.setBiaoqian_1((TextView) convertView.findViewById(R.id.biaoqian_1));
            holder.setBiaoqian_2((TextView) convertView.findViewById(R.id.biaoqian_2));
            holder.setNickname((TextView) convertView.findViewById(R.id.nickname));
            holder.setPazs((TextView) convertView.findViewById(R.id.pazs));
            holder.setPortrait((ImageView) convertView.findViewById(R.id.portrait));
            holder.setSchool((TextView) convertView.findViewById(R.id.school));
            holder.setSex((ImageView) convertView.findViewById(R.id.sex));
            holder.setVerify((ImageView) convertView.findViewById(R.id.verify));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final GoPinData gd = data.get(position);
        if (gd.getSex().equals("F")) {
            holder.sex.setBackgroundResource(R.drawable.big_girl);
        } else {
            holder.sex.setBackgroundResource(R.drawable.big_boy);
        }
        if (gd.getIsVerify().equals("0")) {
            holder.getVerify().setVisibility(View.GONE);
        } else {
            holder.getVerify().setVisibility(View.VISIBLE);
       }
        holder.getNickname().setText(gd.getNickname());
        holder.getSchool().setText(gd.getSchool());
        int point =Integer.parseInt(gd.getPoints());
        if (point<=15) {
            holder.getPazs().setBackgroundResource(pazss[0]);
        }else if(point>15&&point<=50){
            holder.getPazs().setBackgroundResource(pazss[3]);
        }else if(point>50&&point<=100){
            holder.getPazs().setBackgroundResource(pazss[2]);
        }else if(point>100&&point<=300){
            holder.getPazs().setBackgroundResource(pazss[1]);
        }else if(point>300){
            holder.getPazs().setBackgroundResource(pazss[4]);
        }
        holder.getPazs().setText(gd.getPoints());
        holder.getBiaoqian_1().setVisibility(View.VISIBLE);
        holder.getBiaoqian_2().setVisibility(View.VISIBLE);
        String[] tags = setTagToString(gd.getTag_str());
        List<String> tagName =SqliteUtils.getInstance().getTag(context, tags);
        if (tagName.size()==2) {
            int random1=  new Random().nextInt(4);
            holder.getBiaoqian_1().setText(tagName.get(0));
            holder.getBiaoqian_2().setText(tagName.get(1));
            holder.getBiaoqian_1().setBackgroundResource(bqs[random1]);
            holder.getBiaoqian_2().setBackgroundResource(bqs[random1 + 1 > 3 ? 1 : 3]);
        }else if (tagName.size()==1){
            holder.getBiaoqian_1().setText(tagName.get(0));
            int random =  new Random().nextInt(4);
            holder.getBiaoqian_1().setBackgroundResource(bqs[random]);
            holder.getBiaoqian_2().setVisibility(View.GONE);
        }else{
            holder.getBiaoqian_1().setVisibility(View.GONE);
            holder.getBiaoqian_2().setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AtyGoPinProfile.class);
                String receiverId = gd.getId();
                i.putExtra("receiverId", receiverId);
                context.startActivity(i);
            }
        });
        ImageView pic = holder.getPortrait();
        pic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               context.startActivity(new Intent(context, AtyShowPortrait.class).putExtra("name", gd.getPortrait()));
            }
        });
        AtySupport atySupport = (AtySupport) context;
        UserTools.displayImage(gd.getPortrait(), pic, atySupport.getOptions());
        return convertView;
    }
    /**
     * 把标签转换成数组
     *
     * @param tag 标签字符串
     * @return String[]
     */
    private String[] setTagToString(String tag) {
        StringBuffer sb = new StringBuffer(tag);
        if (sb.length() == 0) {
            return new String[]{""};
        }
        if(tag.contains(",")){
            return tag.split(",");
        }else{
            return new String[]{tag};
        }
    }
    private class Holder {
        private ImageView portrait;
        private ImageView verify;
        private ImageView sex;
        private TextView school;
        private TextView nickname;
        private TextView pazs;
        private TextView biaoqian_1;
        private TextView biaoqian_2;

        public ImageView getPortrait() {
            return portrait;
        }

        public void setPortrait(ImageView portrait) {
            this.portrait = portrait;
        }

        public ImageView getVerify() {
            return verify;
        }

        public void setVerify(ImageView verify) {
            this.verify = verify;
        }

        public ImageView getSex() {
            return sex;
        }

        public void setSex(ImageView sex) {
            this.sex = sex;
        }

        public TextView getSchool() {
            return school;
        }

        public void setSchool(TextView school) {
            this.school = school;
        }

        public TextView getNickname() {
            return nickname;
        }

        public void setNickname(TextView nickname) {
            this.nickname = nickname;
        }

        public TextView getPazs() {
            return pazs;
        }

        public void setPazs(TextView pazs) {
            this.pazs = pazs;
        }

        public TextView getBiaoqian_1() {
            return biaoqian_1;
        }

        public void setBiaoqian_1(TextView biaoqian_1) {
            this.biaoqian_1 = biaoqian_1;
        }

        public TextView getBiaoqian_2() {
            return biaoqian_2;
        }

        public void setBiaoqian_2(TextView biaoqian_2) {
            this.biaoqian_2 = biaoqian_2;
        }
    }
}
