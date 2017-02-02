package com.jl.atys.individualcenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.customs.ConfirmAlertDialog;
import com.jl.customs.DrawCloseLinearLayout;
import com.jl.customs.DrawLinearLayout;
import com.jl.customs.TagDialog;
import com.jl.customs.age.NumericWheelProAdpter;
import com.jl.customs.age.NumericWheelSalaryAdapter;
import com.jl.customs.age.NumericWheelXZAdapter;
import com.jl.customs.age.YearDialog;
import com.jl.customs.timer.TimerDialog;
import com.jl.db.SqliteUtils;
import com.jl.domain.PersonBean;
import com.jl.net.Complete;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hrb.jl.pinai.R;

/**
 * 类描述：个人信息修改
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-23
 * 上午10:02:49
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyIndRevamp extends AtySupport implements OnClickListener {
    private TextView tv_nc, tv_birth, tv_school, tv_year, tv_xz,
            tv_ah, tv_jj, tv_aqxy, tv_zajy,tv_province,tv_salary;
    private PersonBean personBean;//传过来的个人资料
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String[] ss = (String[]) msg.obj;
                    String[] tags = setTagToString(personBean.getTag_str());
                    setTag(removeRepeat(tags, ss)); //把点击获取的和原来的标签进行对比，如果有重复则不显示
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    //总共标签数
    private int sumTag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initTitle();
        try {
            //接收信息
            personBean = (PersonBean) getIntent().getExtras().get("ip");
        } catch (Exception e) {
            personBean = new PersonBean("", "", "", "", "", "", "", "", "", "", "", "", "");
        }
        setUser();
        setTag();
    }

    private void initTitle() {
        // ---------------------------设置标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_ind_revamp);
        setSystemBar(0);
    }

    public void back(View v) {
        //退出
        ConfirmAlertDialog.Builder builder = new ConfirmAlertDialog.Builder(
                context);
        builder.setTitle("提示").setContent("确认退出修改吗？").setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String content = "";
        try {
            content = data.getExtras().getString("content");
        } catch (Exception ignored) {
        }
        if (TextUtils.isEmpty(content)) {
            return;
        }
        switch (requestCode) {
            case R.id.ind_rap_nc:
                personBean.setNickname(content);
                //这个是设置ios离线推送的到时候应该加到个人中心里面
                //PinApplication.currentUserNick =content;
//                boolean updateNick = EMChatManager.getInstance().updateCurrentUserNick(content);
//                if (!updateNick) {
//                    EMLog.e("e", "更新昵称失败");
//                }
                tv_nc.setText(getTwoColorText("昵称", content));
                break;
            case R.id.ind_rap_school:
                personBean.setSchool(content);
                tv_school.setText(getTwoColorText("学校", content));
                break;
            case R.id.ind_rap_ah:
                personBean.setHobbies(content);
                tv_ah.setText(getTwoColorText("爱好", content));
                break;
            case R.id.ind_rap_jj:
                personBean.setSelf_intro(content);
                tv_jj.setText(getTwoColorText("个人简介", content));
                break;
            case R.id.ind_rap_question:
                personBean.setQuestion(content);
                tv_aqxy.setText(getTwoColorText(getResources().getString(R.string.aiqingxuanyuan), content));
                break;
            case R.id.ind_rap_zajy:
                personBean.setBio(content);
                tv_zajy.setText(getTwoColorText("真爱寄语", content));
                break;
            default:
                break;
        }
    }

    /**
     * 设置标签 setTag
     *
     * @since 1.0.0
     */
    private void setTag() {

        String[] tags = setTagToString(personBean.getTag_str());
        sumTag = tags.length;
        List<String> tagName = SqliteUtils.getInstance().getTag(context, tags);
        ViewGroup vg = (ViewGroup) findViewById(R.id.ind_rap_flowLayout);
        for (int i = 0; i < tagName.size(); i++) {
            DrawCloseLinearLayout dclt = new DrawCloseLinearLayout(context, tagName.get(i), i, new DrawCloseLinearLayout.CloseCallback() {
                @Override
                public void onCloseCallback(DrawCloseLinearLayout dlt) {
                    String[] tags = setTagToString(personBean.getTag_str());
                    Set<String> set = new HashSet<String>();
                    for (String tag : tags) {
                        set.add(tag.trim());
                    }
                    Log.e("This", String.valueOf(dlt.getThisI()));
                    set.remove(String.valueOf(dlt.getThisI()));
                    String[] newStr = set.toArray(new String[1]);
                    String toString = Arrays.toString(newStr);
                    if (!toString.equals("[null]")) {
                        Log.e("Test3", toString);
                        personBean.setTag_str(toString);
                    } else {
                        personBean.setTag_str("");
                    }
                    dlt.setVisibility(View.GONE);
                }
            });
            try {
                int tag = Integer.valueOf(tags[i].trim());
                dclt.setThisI(tag);
                dclt.setIv(R.drawable.xx);
                vg.addView(dclt);
            } catch (Exception e) {
                Log.e("e", "tag");
            }
        }
        DrawLinearLayout dlt = new DrawLinearLayout(context, "标签", 1,
                new DrawLinearLayout.CloseCallback() {
                    @Override
                    public void onCloseCallback() {
                        final TagDialog.Builder builder = new TagDialog.Builder(
                                context);
                        builder.setTitle("请选择标签").setPositiveButton("确认",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Set<String> tag = builder.getSelectTag();
                                        String[] toBeStored = tag.toArray(new String[tag.size()]);
                                        Message msg = new Message();
                                        msg.obj = toBeStored;
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
                    }
                });
        vg.addView(dlt);
    }

    /**
     * 点击添加tag
     *
     * @param tags
     */
    private void setTag(String[] tags) {
        String toString = Arrays.toString(tags);
        if (!toString.equals("[null]")) {
            Log.e("Test1", toString);
            personBean.setTag_str(toString);
        } else {
            personBean.setTag_str("");
        }
        List<String> tagName = SqliteUtils.getInstance().getTag(context, tags);
        ViewGroup vg = (ViewGroup) findViewById(R.id.ind_rap_flowLayout);
        //删除所有重新添加
        vg.removeAllViews();
        for (int i = 0; i < tagName.size(); i++) {
            DrawCloseLinearLayout dclt = new DrawCloseLinearLayout(context, tagName.get(i), i, new DrawCloseLinearLayout.CloseCallback() {
                @Override
                public void onCloseCallback(DrawCloseLinearLayout dlt) {
                    String[] tags = setTagToString(personBean.getTag_str());
                    Set<String> set = new HashSet<String>();
                    for (String tag : tags) {
                        set.add(tag.trim());
                    }
                    Log.e("ThisI", String.valueOf(dlt.getThisI()));
                    set.remove(String.valueOf(dlt.getThisI()));
                    String[] newStr = set.toArray(new String[1]);
                    sumTag = newStr.length;
                    String toString = Arrays.toString(newStr);
                    if (!toString.equals("[null]")) {
                        Log.e("Test2", toString);
                        personBean.setTag_str(toString);
                    } else {
                        personBean.setTag_str("");
                    }
                    dlt.setVisibility(View.GONE);
                }
            });
            try {
                int tag = Integer.valueOf(tags[i].trim());
                dclt.setThisI(tag);
            } catch (Exception e) {
                dclt.setThisI(0);
            }
            dclt.setIv(R.drawable.xx);
            vg.addView(dclt);
        }
        DrawLinearLayout dlt = new DrawLinearLayout(context, "标签", 1,
                new DrawLinearLayout.CloseCallback() {
                    @Override
                    public void onCloseCallback() {
                        final TagDialog.Builder builder = new TagDialog.Builder(
                                context);
                        builder.setTitle("请选择标签").setPositiveButton("确认",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //选择的标签
                                        Set<String> tag = builder.getSelectTag();

                                        String[] toBeStored = tag.toArray(new String[tag.size()]);
                                        Message msg = new Message();
                                        msg.obj = toBeStored;
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
                    }
                });
        vg.addView(dlt);
    }

    private void setUser() {
        tv_nc = (TextView) findViewById(R.id.ind_rap_nc);
        tv_nc.setText(getTwoColorText("昵称", personBean.getNickname()));
        tv_nc.setTag(personBean.getNickname());
        tv_nc.setOnClickListener(this);
        tv_birth = (TextView) findViewById(R.id.ind_rap_birth);
        tv_birth.setText(getTwoColorText("出生年", personBean.getBorn_year()));
        tv_birth.setOnClickListener(this);
        tv_school = (TextView) findViewById(R.id.ind_rap_school);
        tv_school.setText(getTwoColorText("学校", personBean.getSchool()));
        tv_school.setOnClickListener(this);
        tv_year = (TextView) findViewById(R.id.ind_rap_year);
        tv_year.setText(getTwoColorText("入学年", personBean.getGrade()));
        tv_year.setOnClickListener(this);
        tv_xz = (TextView) findViewById(R.id.ind_rap_xz);
        tv_xz.setText(getTwoColorText("星座", personBean.getConstellation()));
        tv_xz.setOnClickListener(this);
        tv_ah = (TextView) findViewById(R.id.ind_rap_ah);
        tv_ah.setText(getTwoColorText("爱好", personBean.getHobbies()));
        tv_ah.setOnClickListener(this);
        tv_ah.setTag(personBean.getHobbies());
        tv_jj = (TextView) findViewById(R.id.ind_rap_jj);
        tv_jj.setText(getTwoColorText("个人简介", personBean.getSelf_intro()));
        tv_jj.setOnClickListener(this);
        tv_jj.setTag(personBean.getSelf_intro());
        tv_aqxy = (TextView) findViewById(R.id.ind_rap_question);
        tv_aqxy.setText(getTwoColorText("爱情考验", personBean.getQuestion()));
        tv_aqxy.setOnClickListener(this);
        tv_aqxy.setTag(personBean.getQuestion());
        tv_zajy = (TextView) findViewById(R.id.ind_rap_zajy);
        tv_zajy.setText(getTwoColorText("真爱寄语", personBean.getBio()));
        tv_zajy.setOnClickListener(this);
        tv_zajy.setTag(personBean.getBio());
        tv_salary = (TextView) findViewById(R.id.ind_rap_salary);
        tv_salary.setText(getTwoColorText("薪资", personBean.getSalary()));
        tv_salary.setOnClickListener(this);
        tv_province = (TextView) findViewById(R.id.ind_rap_province);
        tv_province.setText(getTwoColorText("所在地", personBean.getProvince()));
        tv_province.setOnClickListener(this);
        Button submit = (Button) findViewById(R.id.ind_rap_btn_edit);
        submit.setOnClickListener(this);
    }

    private SpannableStringBuilder getTwoColorText(String str1, String str2) {
        SpannableStringBuilder style = new SpannableStringBuilder(str1 + "：" + str2);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background_pink)), 0, str1.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    /**
     * 去掉两个数组中的重复
     *
     * @param a1
     * @param a2
     * @return
     */
    private String[] removeRepeat(String[] a1, String[] a2) {
        Set<String> set = new HashSet<>();
        for (String s : a1) {
            String t = s + ",";
            set.add(t.trim());
        }
        for (String s : a2) {
            String t = s + ",";
            set.add(t.trim());
        }
        String result = "";
        for (String s : set) {
            result += s;
        }
        result = result.substring(0, result.length() - 1);     //去掉最后一个“，”号
        String[] st = result.split(",");
        Log.i("e", result);
        sumTag = st.length;
        return st;
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

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {


            case R.id.ind_rap_nc:
                i = new Intent(AtyIndRevamp.this, AtyIndRevampItems.class);
                i.putExtra("tag", "昵称");
                i.putExtra("count", Config.MAX_CHAR_NAME_COUNT);
                i.putExtra("content", (String) v.getTag());
                break;

            case R.id.ind_rap_school:
                Intent intent = new Intent(AtyIndRevamp.this, AtySchoolChoose.class);
                startActivityForResult(intent, v.getId());
                break;
            case R.id.ind_rap_birth:
                //出生年
                setTimerSelect(Config.MIN_BIRTH, Config.MAX_BIRTH);
                break;
            case R.id.ind_rap_year:
                //入学年
                setTimerSelect(Config.MIN_GRADE, Config.MAX_GRADE);
                break;
            case R.id.ind_rap_province:
                //省份
                setTimerSelect(Config.MIN_PROVINCE, Config.MAX_PROVINCE);
                break;
            case R.id.ind_rap_salary:
                //薪资
                setTimerSelect(Config.MIN_SALARY, Config.MAX_SALARY);
                break;
            case R.id.ind_rap_xz:
                //星座
                setTimerSelect(Config.MIN_CON, Config.MAX_CON);
                break;
            case R.id.ind_rap_ah:
                i = new Intent(AtyIndRevamp.this, AtyIndRevampItems.class);
                i.putExtra("tag", "爱好");
                i.putExtra("count", Config.MAX_CHAR_COUNT);
                i.putExtra("content", (String) v.getTag());
                break;
            case R.id.ind_rap_jj:
                i = new Intent(AtyIndRevamp.this, AtyIndRevampItems.class);
                i.putExtra("tag", "个人简介");
                i.putExtra("count", Config.MAX_CHAR_COUNT);
                i.putExtra("content", (String) v.getTag());
                break;
            case R.id.ind_rap_question:
                i = new Intent(AtyIndRevamp.this, AtyIndRevampItems.class);
                i.putExtra("tag", "爱情考验");
                i.putExtra("count", Config.MAX_CHAR_COUNT);
                i.putExtra("content", (String) v.getTag());
                break;
            case R.id.ind_rap_zajy:
                i = new Intent(AtyIndRevamp.this, AtyIndRevampItems.class);
                i.putExtra("tag", "真爱寄语");
                i.putExtra("count", Config.MAX_CHAR_ZAJY_COUNT);
                i.putExtra("content", (String) v.getTag());
                break;
            //点击保存
            case R.id.ind_rap_btn_edit:
                submit();

                break;
            default:
                break;
        }
        if (null != i) {
            AtyIndRevamp.this.startActivityForResult(i, v.getId());
        }
    }

    /**
     * 提交数据
     */
    private void submit() {
        //进行未填写处理
        if (!checkedNull()) {
            return;
        }
        showProgressDialog(context, "提示", "修改中", false);
        //这里进行数据转换 - -苦逼啊 TAT
        //头像地址这里目前不需要
        String portrait = "";
        //-----------这里对tag进行转换
        String tags = personBean.getTag_str().replace("\"", "");
        tags = tags.replace(" ", "");
        tags = tags.replace(",,", ",");
        tags = "," + tags.substring(1, tags.length() - 1);
        tags = tags.replace(",,", ",");
        Log.e("E", tags);
        //-----------这里对星座进行转换
        String con = personBean.getConstellation().replace("座", "");
        String c = "";
        switch (con) {
            case "水瓶":
                c = "1";
                break;
            case "双鱼":
                c = "2";
                break;
            case "白羊":
                c = "3";
                break;
            case "金牛":
                c = "4";
                break;
            case "双子":
                c = "5";
                break;
            case "巨蟹":
                c = "6";
                break;
            case "狮子":
                c = "7";
                break;
            case "处女":
                c = "8";
                break;
            case "天秤":
                c = "9";
                break;
            case "天蝎":
                c = "10";
                break;
            case "射手":
                c = "11";
                break;
            case "摩羯":
                c = "12";
                break;
        }


        new Complete(Config.getCacheID(context), personBean.getNickname(),
                //这里如果有头像地址就传头像文件名，如果没有就不传
                c, portrait,
                tags, personBean.getSex(), personBean.getBorn_year(),
                personBean.getGrade(), personBean.getHobbies(), personBean.getSelf_intro(),
                personBean.getBio(), personBean.getQuestion(), personBean.getSchool(),personBean.getSalaryId(),personBean.getProvinceId() ,new Complete.SuccessCallback() {
            @Override
            public void onSuccess() {
                showToast("修改成功");
                closeProgressDialog();
                Intent ai = new Intent(AtyIndRevamp.this, AtyIndCheckOutMy.class);
                setResult(RESULT_OK, ai);
                finish();
            }
        }, new Complete.FailCallback() {

            @Override
            public void onFail(int code) {
                closeProgressDialog();
                showToast("修改失败");
            }
        });
    }

    /**
     * 检查未填写的数据
     */
    private boolean checkedNull() {
        if (TextUtils.isEmpty(personBean.getNickname())) {
            showToast("请填写昵称哟");
            return false;
        } else if (TextUtils.isEmpty(personBean.getBorn_year())) {
            showToast("请填写出生年哟");
            return false;
        } else if (TextUtils.isEmpty(personBean.getConstellation()) || personBean.getConstellation().equals("未选择星座")) {
            showToast("请填写星座哟");
            return false;
        } else if (TextUtils.isEmpty(personBean.getSchool())) {
            showToast("请填写学校哟");
            return false;
        } else if (TextUtils.isEmpty(personBean.getGrade())) {
            showToast("请填写入学年哟");
            return false;
        }
        else if (TextUtils.isEmpty(personBean.getSalary())) {
            showToast("请填写薪资哟");
            return false;
        }
        else if (TextUtils.isEmpty(personBean.getProvince())) {
            showToast("请填写所在地哟");
            return false;
        }
        else if (TextUtils.isEmpty(personBean.getTag_str())) {
            showToast("请填写标签哟");
            return false;
        } else if (TextUtils.isEmpty(personBean.getHobbies())) {
            showToast("请填写爱好哟");
            return false;
        } else if (TextUtils.isEmpty(personBean.getSelf_intro())) {
            showToast("请填写个人简介哟");
            return false;
        } else if (TextUtils.isEmpty(personBean.getBio())) {
            showToast("请填写真爱寄语哟");
            return false;
        } else if (TextUtils.isEmpty(personBean.getQuestion())) {
            showToast("请填写爱情考验哟");
            return false;
        } else if (sumTag > 6) {
            showToast("标签最多只能 填写6个哟");
            return false;
        } else {
            return true;
        }
    }
    /**
     * 年月日时间选择器 setTime
     * 现在没有用到
     * void
     *
     * @since 1.0.0
     */
    private void setTime() {
        TimerDialog.Builder builder = new TimerDialog.Builder(context,
                AtyIndRevamp.this);
        builder.setPositiveButton(new TimerDialog.PositiveListener() {
            @Override
            public void onClick(DialogInterface dialog, String time) {
                showToast(time);
                dialog.cancel();
            }
        });
        builder.setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void setTimerSelect(final int start, int end) {
        YearDialog.Builder builder = new YearDialog.Builder(context,
                AtyIndRevamp.this);
        builder.setPositiveButton(new YearDialog.PositiveListener() {
            @Override
            public void onClick(DialogInterface dialog, String time) {
                if (start == Config.MIN_CON) {
                    //星座
                    String xz = NumericWheelXZAdapter.XING_ZUO[Integer.parseInt(time)];
                    personBean.setConstellation(xz);
                    tv_xz.setText(getTwoColorText("星座", xz + "座"));
                } else if (start == Config.MIN_BIRTH) {
                    //出生年
                    personBean.setBorn_year(time);
                    tv_birth.setText(getTwoColorText("出生年", time));
                } else if (start == Config.MIN_GRADE) {
                    //入学年
                    personBean.setGrade(time);
                    tv_year.setText(getTwoColorText("入学年", time));
                }else if (start == Config.MIN_SALARY) {
                    String salary = NumericWheelSalaryAdapter.SALARY[Integer.parseInt(time)-5];
                    //薪资
                    personBean.setSalaryId((Integer.parseInt(time)-5)+"");
                    tv_salary.setText(getTwoColorText("薪资", salary));
                }else if (start == Config.MIN_PROVINCE) {
                    String province = NumericWheelProAdpter.PROVINCE[Integer.parseInt(time)-1];
                    //省份
                    personBean.setProvinceId(time);

                    tv_province.setText(getTwoColorText("所在地", province));
                }
                dialog.cancel();
            }
        });
        builder.setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create(start, end).show();
    }
}
