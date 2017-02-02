
package com.jl.atys.dsgy.zph;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jl.atys.gopin.AtyGoPinProfile;
import com.jl.basic.AtySupport;
import com.jl.domain.GetLikeJobBean;
import com.jl.net.GetLikeJob;
import com.jl.utils.UserTools;

import hrb.jl.pinai.R;

public class AtyDsgyOpen extends AtySupport {
    private ImageView portrait;
    private TextView title;
    private TextView content;
    private RelativeLayout r1;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_dsgy_open);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        portrait= (ImageView) findViewById(R.id.portrait);
        r1= (RelativeLayout) findViewById(R.id.r1);

       String id = getIntent().getStringExtra("id");
       new GetLikeJob(id, new GetLikeJob.SuccessCallback() {
           @Override
           public void onSuccess(GetLikeJobBean data) {
                if (data.getSex().equals("F")){
                    r1.setBackgroundResource(R.color.linear_line);
                    content.setBackgroundResource(R.color.background_pink);
                    //setStatusBarTint(AtyDsgyOpen.this, getResources().getColor(R.color.linear_line));

                }else{
                    findViewById(R.id.top_bar).setBackgroundResource(R.color.linear_line2);
                    //setStatusBarTint(AtyDsgyOpen.this, getResources().getColor(R.color.linear_line2));
                }
               title.setText(data.getTitle());
               //
               content.setText(data.getContent());
              // content.setText("我爱吃大香蕉，还爱喝纯牛奶，你呢小宝贝哈哈哈哈哈哈\n\n 要求： \n 1. 你好\n 2. 你好\n 3. \n 4. \n 5. ");
               UserTools.displayImage(data.getPortrait(), portrait, getOptions());
               userId=data.getUserId();
               findViewById(R.id.playout).setVisibility(View.GONE);
           }
       }, new GetLikeJob.FailCallback() {
           @Override
           public void onFail(String error) {

           }
       });
        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AtyGoPinProfile.class);
                i.putExtra("receiverId", userId);
                context.startActivity(i);
            }
        });
    }
    public void back(View v) {
        finish();
    }
}
