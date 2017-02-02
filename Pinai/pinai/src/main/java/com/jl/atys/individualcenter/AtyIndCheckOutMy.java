package com.jl.atys.individualcenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.customs.DrawLinearLayout;
import com.jl.db.SqliteUtils;
import com.jl.domain.PersonBean;
import com.jl.net.Account;
import com.jl.utils.UserTools;

import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类描述：个人信息查看
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-23
 * 上午10:02:49
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyIndCheckOutMy extends AtySupport {
    private final int toRevamp = 10000;//发送到修改页面的标识符
    private PersonBean personBean;
    private ImageView pic;//头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle();
        setSystemBar(0);
        loadData();
    }

    public void back(View v) {
        finish();
    }

    private void initTitle() {
        // ---------------------------设置标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_ind_checkout_my);
    }

    private void loadData() {
        new Account(Config.getCacheID(context), new Account.SuccessCallback() {
            @Override
            public void onSuccess(PersonBean ip) {
                personBean = ip;
                loadData(ip);
                //关闭进度等待
                findViewById(R.id.playout).setVisibility(View.GONE);
            }
        }, new Account.FailCallback() {
            @Override
            public void onFail(String error) {

            }
        });
    }

    private void loadData(PersonBean ip) {
        //((TextView) findViewById(R.id.points)).setText("聘爱指数：" + personBean.getPoints());
        ((TextView) findViewById(R.id.ind_ckt_nc)).setText(personBean.getNickname());
        ((TextView) findViewById(R.id.ind_ckt_zajy)).setText("聘爱宣言：" + personBean.getBio());
        ((TextView) findViewById(R.id.ind_ckt_grade)).setText("入学年：" + personBean.getGrade());
        ((TextView) findViewById(R.id.ind_ckt_age)).setText("出生年：" + personBean.getBorn_year());
        ((TextView) findViewById(R.id.ind_ckt_school)).setText("学校：" + personBean.getSchool());
        ((TextView) findViewById(R.id.ind_ckt_xz)).setText("星座：" + personBean.getConstellation());
        ((TextView) findViewById(R.id.ind_ckt_province)).setText("所在地：" + personBean.getProvince());
        ((TextView) findViewById(R.id.ind_ckt_salary)).setText("薪资：" + personBean.getSalary());
        ((TextView) findViewById(R.id.ind_ckt_ah)).setText("爱好：" + personBean.getHobbies());
        ((TextView) findViewById(R.id.ind_ckt_jj)).setText(personBean.getSelf_intro());
        pic = (ImageView) findViewById(R.id.ind_ckt_touxiang);
        //加载头像
        UserTools.displayImage(ip.getPortrait(), pic, getOptions());
        //点击头像跳转到修改头像页面
        if (ip.getVerify().equals("1")) {
            ImageView verify = (ImageView) findViewById(R.id.verify);
            verify.setVisibility(View.VISIBLE);
        }
        if(personBean.getSex().equals("F")){
            findViewById(R.id.checkout_bar).setBackgroundResource(R.color.background_pink);
            findViewById(R.id.portrait_background).setBackgroundResource(R.color.background_pink);
        }
        //----------------标签---------------------------
        String[] tags = setTagToString(ip.getTag_str());
        List<String> tagName = SqliteUtils.getInstance().getTag(context, tags);
        ViewGroup vg = (ViewGroup) findViewById(R.id.ind_my_flowLayout);
        //删除所有的标签，不然刷新有bug
        vg.removeAllViews();
        for (int i = 0; i < tagName.size(); i++) {
            DrawLinearLayout dclt = new DrawLinearLayout(context, tagName.get(i), i,
                    null);
            vg.addView(dclt);
        }
        //--------------------------------------------------
        Button revamp = (Button) findViewById(R.id.ind_ckt_my_btn_edit);
        revamp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AtyIndCheckOutMy.this,
                        AtyIndRevamp.class);
                Bundle bundle = new Bundle();
                if (personBean != null) {
                    bundle.putSerializable("ip", personBean);
                    i.putExtras(bundle);
                    startActivityForResult(i, toRevamp);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //这里有点问题。更换不了头像。先mark
            // resultCode为回传的标记，我在B中回传的是RESULT_OK
            //是更改头像
            case Config.KEY_FROM_CHECK_MY:
                String portrait = Config.getCachePortrait(context);
                if (portrait != null) {
                    UserTools.displayImage(portrait, pic, getOptions());
                }
                break;
            case toRevamp:

                //如果是从修改页面跳转回来的那就重新加载数据
                loadData();
                break;
            default:
                break;
        }
    }

    private SpannableStringBuilder getTwoColorText(String str1, String str2) {
        SpannableStringBuilder style = new SpannableStringBuilder(str1 + "：" + str2);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background_pink)), 0, str1.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
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
        tag = sb.substring(1, sb.length() - 1);
        tag = tag.replace("\"", " ");
        String[] strings = tag.split(",");
        //SqliteUtils.getInstance().getTag(context);
        return strings;
    }
}
