package com.jl.atys;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jl.atys.chat.domain.InviteMessage;
import com.jl.atys.gopin.AtyShowPortrait;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.customs.DrawLinearLayout;
import com.jl.customs.ReportDialog;
import com.jl.dao.InviteMessgeDao;
import com.jl.db.SqliteUtils;
import com.jl.domain.AcceptBean;
import com.jl.domain.PersonBean;
import com.jl.net.Accept;
import com.jl.net.MembersShow;
import com.jl.net.Reject;
import com.jl.net.Support;
import com.jl.utils.UserTools;

import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类描述：消息透传
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24
 * 下午12:42:11
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyNotify extends AtySupport {
    private String receiverId;
    private InviteMessage msg;//更新的信息
    //private String question;
    private Button acceptBtn;
    private Button refuseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_profile_yes_or_no);
        //追你的人的id
        receiverId = getIntent().getStringExtra("id");
        //根据id获取数据库中的消息
        msg = new InviteMessgeDao(context).getMessage(receiverId);
        acceptBtn = (Button) findViewById(R.id.yes_or_not_accept);
        refuseBtn = (Button) findViewById(R.id.yes_or_not_refuse);
        setInitData();
        yesOrNo(receiverId);


    }

    /**
     * 初始化信息 setInitData
     *
     * @since 1.0.0
     */
    private void setInitData() {
        String userId = Config.getCacheID(context);
        new MembersShow(userId, receiverId, new MembersShow.SuccessCallback() {
            @Override
            public void onSuccess(PersonBean personBean) {
                //已经是好友
                if (personBean.getUserLikeMe().equals("1")) {
                    acceptBtn.setEnabled(false);
                    acceptBtn.setText("已经是好友");
                    refuseBtn.setVisibility(View.GONE);
                }
                loadData(personBean);
                //关闭进度等待
                findViewById(R.id.playout).setVisibility(View.GONE);
            }
        }, new MembersShow.FailCallback() {
            @Override
            public void onFail() {
                showToast("fail");
            }
        });
    }

    public void back(View v) {
        finish();
    }

    private void loadData(final PersonBean personBean) {
        ((TextView) findViewById(R.id.points)).setText("聘爱指数：" + personBean.getPoints());
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
        ImageView pic = (ImageView) findViewById(R.id.ind_ckt_touxiang);
        //加载头像
        UserTools.displayImage(personBean.getPortrait(), pic, getOptions());
        pic.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       context.startActivity(new Intent(context, AtyShowPortrait.class).putExtra("name", personBean.getPortrait()));
                                   }
                               }
        );
        if (personBean.getVerify().equals("1")) {
            ImageView verify = (ImageView) findViewById(R.id.verify);
            verify.setVisibility(View.VISIBLE);
        }
        if (personBean.getSex().equals("F")) {
            findViewById(R.id.checkout_bar).setBackgroundResource(R.color.background_pink);
            findViewById(R.id.portrait_background).setBackgroundResource(R.color.background_pink);
            acceptBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_sex_pink));
            refuseBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_sex_pink));
        }
        //----------------标签---------------------------
        String[] tags = setTagToString(personBean.getTag_str());
        List<String> tagName = SqliteUtils.getInstance().getTag(context, tags);
        ViewGroup vg = (ViewGroup) findViewById(R.id.ind_my_flowLayout);
        for (int i = 0; i < tagName.size(); i++) {
            DrawLinearLayout dclt = new DrawLinearLayout(context, tagName.get(i), i,
                    null);
            vg.addView(dclt);
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
        tag = tag.replace("\" \",", "");
        tag = tag.replace("\"", " ");
        String[] strings = tag.split(",");
        return strings;
    }

    private void yesOrNo(final String receiverId) {
        //这里做同意拒绝处理


        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(context, "", "添加中...", false);
                new Accept(Config.getCacheID(context), receiverId, new Accept.SuccessCallback() {
                    @Override
                    public void onSuccess(AcceptBean ab) {
                        ContentValues values = new ContentValues();
                        msg.setStatus(InviteMessage.InviteMesageStatus.AGREED);
                        values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                        InviteMessgeDao messgeDao = new InviteMessgeDao(context);
                        messgeDao.updateMessage(msg.getId(), values);
                        Config.setCacheNewFriendUnRead(context, Config.getCacheNewFriendUnRead(context) - 1);
                        sendBroToContactAll(ab);
                        closeProgressDialog();
                        showToast("添加成功");
                    }
                }, new Accept.FailCallback() {
                    @Override
                    public void onFail(String error) {
                        showToast("未知错误");
                        closeProgressDialog();
                    }
                });
                closeProgressDialog();
            }
        });
        refuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(context, "", "正在处理...", false);
                new Reject(Config.getCacheID(context), receiverId, new Reject.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        ContentValues values = new ContentValues();
                        msg.setStatus(InviteMessage.InviteMesageStatus.REFUSED);
                        values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                        InviteMessgeDao messgeDao = new InviteMessgeDao(context);
                        messgeDao.updateMessage(msg.getId(), values);
                        Config.setCacheNewFriendUnRead(context, Config.getCacheNewFriendUnRead(context) - 1);
                        sendBroToContactAll(null);
                        closeProgressDialog();
                        showToast("已拒绝");
                    }
                }, new Reject.FailCallback() {
                    @Override
                    public void onFail(String error) {
                        closeProgressDialog();
                        showToast("未知错误");
                    }
                });
            }
        });
    }

    /**
     * 刷新通讯录界面
     * sendBroToContactAll
     *
     * @since 1.0.0
     */
    private void sendBroToContactAll(AcceptBean acceptBean) {
        Intent intent = new Intent();
        intent.setAction(Config.RECEIVER_ADD_USER);
        Bundle bundle = new Bundle();
        bundle.putSerializable("acceptBean", acceptBean);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        NotificationManager nNotificaitonMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nNotificaitonMan.cancel(0);
    }

    /**
     * 举报用户
     * @param v
     */
    public void report(View v) {
        final ReportDialog.Builder builder = new ReportDialog.Builder(
                context);
        builder.setPositiveButton("提交",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        String answer = builder.getAnswer();
                        if (!TextUtils.isEmpty(answer)) {
                            showProgressDialog("","提交中...",false);
                            new Support(Config.getCacheID(context), answer, receiverId, new Support.SuccessCallback() {
                                @Override
                                public void onSuccess() {
                                    showToast("举报成功，感谢您的反馈");
                                }
                            }, new Support.FailCallback() {
                                @Override
                                public void onFail(String error) {

                                }
                            });
                            closeProgressDialog();
                            dialog.dismiss();
                        } else {
                            closeProgressDialog();
                            showToast("输入举报内容呀");
                        }
                    }
                });
        builder.create().show();
    }
}
