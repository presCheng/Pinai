package com.jl.atys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.EMLog;
import com.jl.atys.chat.domain.User;
import com.jl.atys.individualcenter.AtyIndService;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.basic.Constant;
import com.jl.basic.PinApplication;
import com.jl.dao.UserDao;
import com.jl.db.CopySqliteForSdCard;
import com.jl.db.DatabaseContext;
import com.jl.net.SignUp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hrb.jl.pinai.R;

/**
 * 类描述：注册页面
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24
 * 下午12:42:11
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyRegist extends AtySupport implements OnClickListener {
    private CheckBox serviceCheck;//同意服务条款
    private String sex;//男女
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                showToast("注册失败");
                closeProgressDialog();
            } else if (msg.what == 2) {
                String m = (String) msg.obj;
                showToast(m);
                closeProgressDialog();
            } else if (msg.what == 1) {
                showToast("注册成功");
                closeProgressDialog();
            }
            return false;
        }
    });
    private TextView registUserName;
    private EditText registPwd;
    private EditText registQuerenpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_regist);
        String phone = getIntent().getStringExtra("phone");
        findViewById(R.id.regist_to_login).setOnClickListener(this);
        registUserName = (TextView) findViewById(R.id.forgot_username);
        registUserName.setText(phone);
        registPwd = (EditText) findViewById(R.id.forgot_old_pwd);
        registQuerenpwd = (EditText) findViewById(R.id.forgot_confirm_pwd);
        findViewById(R.id.button2).setOnClickListener(this);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.sex);
        final RadioButton male = (RadioButton) findViewById(R.id.male);
        final RadioButton female = (RadioButton) findViewById(R.id.female);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == male.getId()) {
                    showToast("为了保证用户真实性，男女选择之后就不能更改了哦");
                    sex = "M";
                } else if (checkedId == female.getId()) {
                    sex = "F";
                    showToast("为了保证用户真实性，男女选择之后就不能更改了哦");
                }
            }
        });
        TextView serviceItems = (TextView) findViewById(R.id.serviceitems);
        serviceItems.setText(Html.fromHtml("<u>" + " 精灵服务条款" + "</u>"));
        serviceItems.setOnClickListener(this);
        serviceCheck = (CheckBox) findViewById(R.id.checkbox);

    }

    private void saveUserData(final String username, final String password) {
        // 登陆成功，保存用户名密码
        PinApplication.getInstance().setUserName(username);
        PinApplication.getInstance().setPassword(password);
        writeSqlite();//根据用户名来写数据库
        try {
            // ** 第一次登录或者之前logout后，加载所有本地群和回话
            // login
            EMGroupManager.getInstance().loadAllGroups();
            EMChatManager.getInstance().loadAllConversations();
            // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
            List<String> userNames = EMContactManager.getInstance()
                    .getContactUserNames();//这个名字是id号
            EMLog.d("roster", "contacts size: " + userNames.size());
            Map<String, User> userlist = new HashMap<String, User>();
            // 添加user"申请与通知"
            User newFriends = new User();
            newFriends.setName(Constant.NEW_FRIEND_USERNAME);
            newFriends.setUsername(Constant.NEW_FRIEND_USERNAME);
            newFriends.setNick(getString(R.string.get_resume));
            newFriends.setHeader("");
            userlist.put(Constant.NEW_FRIEND_USERNAME, newFriends);
            // 存入内存
            PinApplication.getInstance().setContactList(userlist);
            // 存入db
            UserDao dao = new UserDao(context);
            List<User> users = new ArrayList<User>(userlist.values());
            dao.saveContactList(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Function: 第一次进入写入sql
     *
     * @author 徐志国 DateTime 2014-4-7 下午4:24:38
     */
    private void writeSqlite() {
        InputStream input = getResources().openRawResource(R.raw.pinai);
        String dbDir = DatabaseContext.getDbDir();
        String dbPath = DatabaseContext.getDbPath() + PinApplication.getInstance().getDBrName();
        CopySqliteForSdCard csfs = new CopySqliteForSdCard(input, dbDir, dbPath);
        csfs.copyFile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist_to_login:
                startActivity(new Intent(AtyRegist.this, AtyLogin.class));
                finish();
                break;
            case R.id.button2:
                regist();
                break;
            case R.id.serviceitems:
                startActivity(new Intent(AtyRegist.this, AtyIndService.class).putExtra("url", Config.SERVER_SERVICE_URL));
                break;
        }
    }

    /**
     * 点击按钮注册
     */
    private void regist() {
        if (!serviceCheck.isChecked()) {
            showToast("请先阅读并同意服务条款");
            return;
        }
        if (TextUtils.isEmpty(sex)) {
            showToast("请选择性别");
            return;
        }
        String pwd = registPwd.getText().toString();
        String queString = registQuerenpwd.getText().toString();
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(queString)) {
            showToast(getString(R.string.pwd_is_not_empty));
        } else if (TextUtils.equals(pwd, queString)) {
            final String userName = registUserName.getText().toString();
            // ==========================================网络注册
            showProgressDialog("注册中", "请稍后", true);
            new SignUp(userName, pwd, sex, Config.KEY_FROM_ANDROID, new SignUp.SuccessCallback() {
                @Override
                public void onSuccess(final String id,
                                      final String password) {
                    Config.setCacheID(context, id);//把id缓存到本机
                    Config.setCacheUserName(context, userName);//把帐号缓存到手机
                    Config.setCacheSex(context, sex);//把性别缓存到本机
                    EMChatManager.getInstance().login(id, password,
                            new EMCallBack() {
                                @Override
                                public void onError(int arg0,
                                                    String arg1) {
                                    Message msg = new Message();
                                    msg.obj = arg1;
                                    msg.what = 2;
                                    handler.sendMessage(msg);
                                }

                                @Override
                                public void onProgress(int arg0,
                                                       String arg1) {
                                }

                                @Override
                                public void onSuccess() {
                                    handler.sendEmptyMessage(1);
                                    saveUserData(id, password);
                                    // 进入主页面
                                    startActivity(new Intent(
                                            AtyRegist.this,
                                            AtyMain.class));
                                    finish();
                                }
                            });
                }
            }, new SignUp.FailCallback() {
                @Override
                public void onFail() {
                    closeProgressDialog();
                    handler.sendEmptyMessage(0);
                }
            });
        } else if (!TextUtils.equals(pwd, queString)) {
            showToast("密码帐号要一致");
        }
    }
}
