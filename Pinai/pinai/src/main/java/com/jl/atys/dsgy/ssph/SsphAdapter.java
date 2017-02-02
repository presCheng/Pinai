package com.jl.atys.dsgy.ssph;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jl.atys.gopin.AtyGoPinProfile;
import com.jl.atys.gopin.AtyShowPortrait;
import com.jl.atys.gopin.GoPinData;
import com.jl.basic.AtySupport;
import com.jl.utils.UserTools;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

public class SsphAdapter extends BaseAdapter {

    private Context context = null;
    private List<GoPinData> data = new ArrayList<>();
    private int []pazss;
    public SsphAdapter(Context context) {
        this.context = context;
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
                    R.layout.row_ssph_cell, null);
            holder = new Holder();
            holder.setNickname((TextView) convertView.findViewById(R.id.nickname));
            holder.setPazs((TextView) convertView.findViewById(R.id.pazs));
            holder.setPortrait((ImageView) convertView.findViewById(R.id.portrait));
            holder.setSchool((TextView) convertView.findViewById(R.id.school));
            holder.setSex((ImageView) convertView.findViewById(R.id.sex));
            holder.setVerify((ImageView) convertView.findViewById(R.id.verify));
            holder.setPaiming((TextView) convertView.findViewById(R.id.ppppp));
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
        holder.getPaiming().setText("");
       if (position==0){
           holder.getPaiming().setBackgroundResource(R.drawable.paiming1);
       }else if(position==1){
           holder.getPaiming().setBackgroundResource(R.drawable.paiming2);
       }else if(position==2){
           holder.getPaiming().setBackgroundResource(R.drawable.paiming3);
       }else{
           holder.getPaiming().setBackgroundResource(R.drawable.ssph);
           holder.getPaiming().setText(position+1+"");
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
    private class Holder {
        private ImageView portrait;
        private ImageView verify;
        private ImageView sex;
        private TextView school;
        private TextView nickname;
        private TextView pazs;
        private TextView paiming;

        public TextView getPaiming() {
            return paiming;
        }

        public void setPaiming(TextView paiming) {
            this.paiming = paiming;
        }

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
    }
}
