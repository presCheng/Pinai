package com.jl.atys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.jl.atys.chat.domain.User;
import com.jl.atys.chat.utils.CommonUtils;
import com.jl.atys.individualcenter.AtyIndService;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.basic.Constant;
import com.jl.basic.PinApplication;
import com.jl.dao.UserDao;
import com.jl.db.CopySqliteForSdCard;
import com.jl.db.DatabaseContext;
import com.jl.domain.GetNicknameBean;
import com.jl.net.GetNickname;
import com.jl.net.Login;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hrb.jl.pinai.R;

/**
 * 类描述：登录界面
 * 创建人：徐志国
 * 修改人：徐志国
 *
 * 修改时间：2014-11-24
 * 下午12:42:11
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyLogin extends AtySupport implements OnClickListener {


    private EditText edtUserName, edtPwd;
    //是否是自动登录
    private boolean autoLogin = false;
    private HashMap<String, GetNicknameBean> map = null;
    //是否加载到头像数据
    private boolean isLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果用户名密码都有，直接进入主页面
        if (PinApplication.getInstance().getUserName() != null
                && PinApplication.getInstance().getPassword() != null) {
            autoLogin = true;
            startActivity(new Intent(AtyLogin.this, AtyMain.class));
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_login);
        init();
    }

    private void init() {
        findViewById(R.id.login_btn_login).setOnClickListener(this);
        edtUserName = (EditText) findViewById(R.id.login_username);
        edtPwd = (EditText) findViewById(R.id.login_pwd);
        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtPwd.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.login_to_regist).setOnClickListener(this);
        findViewById(R.id.login_to_forgot).setOnClickListener(this);
    }

    /**
     * 获取用户头像和昵称
     */
    private void getNickName() {
        new GetNickname(Config.getCacheID(context), new GetNickname.SuccessCallback() {
            @Override
            public void onSuccess(HashMap<String, GetNicknameBean> m) {
                AtyLogin.this.map = m;
                isLoad = true;
            }
        }, new GetNickname.FailCallback() {
            @Override
            public void onFail(String error) {
                isLoad = true;
            }
        });
    }

    /**
     * 登陆环信
     */
    public void login(final String id, final String password) {
        if (!TextUtils.isEmpty(id)
                && !TextUtils.isEmpty(password)) {
            EMChatManager.getInstance().login(id, password,
                    new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            saveUserData(id, password);
                            closeProgressDialog();
                            //进入主页面
                            startActivity(new Intent(AtyLogin.this,
                                    AtyMain.class));
                            finish();
                        }

                        @Override
                        public void onProgress(int progress,
                                               String status) {
                        }

                        @Override
                        public void onError(int code,
                                            final String message) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    closeProgressDialog();
                                    showToast(getString(R.string.login_fail) + message);
                                }
                            });
                        }
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
        if (Config.getCacheUserName(context) != null) {
            edtUserName.setText(Config.getCacheUserName(context));
        }
    }

    /**
     * 保存用户的环信一些数据到数据库
     *
     * @param username 用户名
     * @param password 密码
     */
    private void saveUserData(final String username, final String password) {
        //保存用户名密码
        PinApplication.getInstance().setUserName(username);
        PinApplication.getInstance().setPassword(password);
        writeSqlite();//根据用户名来写数据库
        try {
            // ** 第一次登录或者之前logout后，加载所有本地群和回话
            // login
            //EMGroupManager.getInstance().loadAllGroups();
            EMChatManager.getInstance().loadAllConversations();
            // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
            List<String> usernames = EMContactManager.getInstance()
                    .getContactUserNames();//这个名字是id号
            EMLog.d("roster", "contacts size: " + usernames.size());
            Map<String, User> userlist = new HashMap<>();
            getNickName();
            while (true) {
                if (isLoad) break;
                //必须要加载到数据
            }
            if (map != null) {
                //这里的数据和自己服务器的数据保持一致不和环信的数据库保持一致
                if (map.size() != 0) {
                    for (String id : usernames) {
                        User user = new User();
                        GetNicknameBean idTon = map.get(id);
                        try {
                            //防止从服务器获取的数据异常
                            user.setUsername(id);
                            user.setName(idTon.getNickName());
                            user.setPortrait(idTon.getPortrait());
                            setUserHeader(idTon.getNickName(), user);
                            userlist.put(id, user);
                        } catch (Exception e) {
                            Log.e("login", e.getMessage());
                        }
                    }
                }
            }
            // 添加user"申请与通知"
            User newFriends = new User();
            newFriends.setUsername(Constant.NEW_FRIEND_USERNAME);
            newFriends.setName(Constant.NEW_FRIEND_USERNAME);
            newFriends.setNick(getString(R.string.get_resume));
            newFriends.setHeader("");
            userlist.put(Constant.NEW_FRIEND_USERNAME, newFriends);
            // 存入内存
            PinApplication.getInstance().setContactList(userlist);
            // 存入db
            UserDao dao = new UserDao(context);
            List<User> users = new ArrayList<>(userlist.values());
            dao.saveContactList(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     */
    protected void setUserHeader(String username, User user) {
        String headerName = user.getName();
        boolean isDigit = true;
        try {
            isDigit = Character.isDigit(headerName.charAt(0));
        } catch (Exception ignored) {
        }
        if (username.equals(Constant.NEW_FRIEND_USERNAME)) {
            user.setHeader("");
        } else if (isDigit) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance()
                    .get(headerName.substring(0, 1)).get(0).target.substring(0,
                            1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }

    /**
     * Function:
     * 把一些数据写入到数据库
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
            case R.id.login_btn_login:
                if (!CommonUtils.isNetWorkConnected(context)) {
                    showToast(getString(R.string.network_isnot_available));
                    return;
                }
                showProgressDialog(getString(R.string.prompt), getString(R.string.login_ing), true);
                final String userName = edtUserName.getText().toString();
                String password = edtPwd.getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    closeProgressDialog();
                    showToast(getString(R.string.user_password_not_empty));
                } else {
                    new Login(userName, password, new Login.SuccessCallback() {
                        @Override
                        public void onSuccess(String id, String password, String portrait, String sex) {
                            Config.setCacheID(context, id);//把id缓存到本机
                            Config.setCacheUserName(context, userName);//把帐号缓存到本机
                            Config.setCachePortrait(context, portrait);//把头像地址缓存到本地
                            Config.setCacheSex(context, sex);//把性别缓存到本地
                            //Config.setCacheNickName(context, nickname);
                            login(id, password);
                            showToast(getString(R.string.login_success));
                        }
                    }, new Login.FailCallback() {
                        @Override
                        public void onFail(String error) {
                            closeProgressDialog();
                            showToast("密码或帐号错误");
                        }
                    });
                }
                break;
            case R.id.login_to_regist:
                startActivity(new Intent(context, AtySendSms.class));
                finish();
                break;
            case R.id.login_to_forgot:
                startActivity(new Intent(AtyLogin.this, AtyForgotPwd.class));
                finish();
                break;
            default:
                break;
        }
    }

    public void goToService(View v){
        startActivity(new Intent(AtyLogin.this, AtyIndService.class).putExtra("url", Config.SERVER_SERVICE_URL));
    }
}
