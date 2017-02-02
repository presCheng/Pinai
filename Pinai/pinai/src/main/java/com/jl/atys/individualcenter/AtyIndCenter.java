package com.jl.atys.individualcenter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.jl.atys.AtyMain;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.customs.OnTabActivityResultListener;
import com.jl.net.GetPortrait;
import com.jl.net.ReNew;
import com.jl.net.Uploadportrait;
import com.jl.setavatar.AtyShowAll;
import com.jl.utils.UserTools;
import com.jl.utils.XuDecodeBase64;

import java.io.File;

import hrb.jl.pinai.R;

/**
 * 类描述：个人中心主界面
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-10 下午2:14:01
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyIndCenter extends AtySupport implements OnClickListener, OnTabActivityResultListener {
    private TextView gogoDay;
    private TextView gogo;
    private int goDays = 0;//加油天数
    private TextView nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_ind_center);
        AtyMain parent = (AtyMain) getParent();
        // 放到父类中，可以回调
        parent.putChildIndCenter(this);

        init();
        if ("F".equals(Config.getCacheSex(context))) {
            findViewById(R.id.center_background).setBackgroundResource(R.color.background_pink);
            ((TextView) findViewById(R.id.ind_gogo)).setTextColor(getResources().getColor(R.color.background_pink));
        }
    }

    private void init() {
        nickName = (TextView) findViewById(R.id.ind_nickname);
        nickName.setText(Config.getCacheNickName(context));
        findViewById(R.id.ind_chaser).setOnClickListener(this);
        findViewById(R.id.ind_sent).setOnClickListener(this);
        findViewById(R.id.ind_my_tiezi).setOnClickListener(this);
        findViewById(R.id.ind_feedback).setOnClickListener(this);
        findViewById(R.id.ind_about_us).setOnClickListener(this);
        findViewById(R.id.ind_setting).setOnClickListener(this);
        gogoDay = (TextView) findViewById(R.id.ind_gogo_day);
        SpannableStringBuilder style = getGogoDays(goDays);
        gogoDay.setText(style);
        gogo = (TextView) findViewById(R.id.ind_gogo);
        gogo.setOnClickListener(this);
        //加油天数
        new ReNew(Config.getCacheID(context), "null", new ReNew.SuccessCallback() {
            @Override
            public void onSuccess(String reNewDays) {
                try {

                    gogoDay.setText(getGogoDays(Integer.parseInt(reNewDays)));
                    //大于30天名字变黄
                    if (Integer.parseInt(reNewDays) >= 30) {
                        nickName.setTextColor(getResources().getColor(R.color.text_4));
                    }
                } catch (Exception e) {
                    gogoDay.setText(getGogoDays(0));
                }
            }
        }, new ReNew.FailCallback() {
            @Override
            public void onFail(String error) {
                showToast(error);
            }
        });

        getPortraitAndNickName();
    }

    private void getPortraitAndNickName() {
        final ImageView iv = (ImageView) findViewById(R.id.ind_head);
        String portrait = Config.getCachePortrait(context);
        if (portrait != null) {
            UserTools.displayImage(portrait, iv, getOptions());
        }
        iv.setOnClickListener(this);
        new GetPortrait(Config.getCacheID(context), new GetPortrait.SuccessCallback() {
            @Override
            public void onSuccess(String portrait, String nickname, String verify, String points) {
                Config.setCachePortrait(context, portrait);
                UserTools.displayImage(portrait, iv, getOptions());
                if (verify.equals("1")) {
                    findViewById(R.id.verify).setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(nickname)) {
                    nickName.setText("未设置昵称");
                } else {
                    Config.setCacheNickName(context, nickname);
                    nickName.setText(nickname);
                }
                ((TextView) findViewById(R.id.ind_pazs)).setText("聘爱指数：" + points);
            }
        }, new GetPortrait.FailCallback() {
            @Override
            public void onFail(String error) {

            }
        });
    }

    /**
     * 获得加油天数
     *
     * @return SpannableStringBuilder
     */
    private SpannableStringBuilder getGogoDays(int days) {
        SpannableStringBuilder style = new SpannableStringBuilder("已连续签到" + days + "天");
        if (days >= 100) {
            style.setSpan(new ForegroundColorSpan(Color.RED), 5, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (days >= 10) {
            style.setSpan(new ForegroundColorSpan(Color.RED), 5, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (days >= 0) {
            style.setSpan(new ForegroundColorSpan(Color.RED), 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return style;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ind_chaser:
                startActivity(new Intent(AtyIndCenter.this, AtyIndCheckOutMy.class));
                break;
            case R.id.ind_sent:
                startActivity(new Intent(AtyIndCenter.this, AtyIndSent.class));
                break;
            case R.id.ind_feedback:
                startActivity(new Intent(AtyIndCenter.this, AtyIndFeedBack.class));
                break;
            case R.id.ind_my_tiezi:
                startActivity(new Intent(AtyIndCenter.this, AtyIndMyTieZi.class));
                break;
            case R.id.ind_about_us:
                startActivity(new Intent(AtyIndCenter.this, AtyIndAboutUs.class));
                break;
            case R.id.ind_setting:
                startActivity(new Intent(AtyIndCenter.this, AtyIndSetting.class));
                break;

            case R.id.ind_gogo:
                new ReNew(Config.getCacheID(context), "dorenew", new ReNew.SuccessCallback() {
                    @Override
                    public void onSuccess(String reNewDays) {
                        //goDays++;
                        gogoDay.setText(getGogoDays(Integer.parseInt(reNewDays)));
                        showToast("成功");
                    }
                }, new ReNew.FailCallback() {
                    @Override
                    public void onFail(String error) {
                        showToast(error);
                    }
                });
                break;
            case R.id.ind_head:
                selectPicFromLocal();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
            // 取得裁剪后的图片
            case REQUEST_CODE_ZOOM:
                /**
                 * 非空判断大家一定要验证，如果不验证的话，
                 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，小马只
                 * 在这个地方加下，大家可以根据不同情况在合适的
                 * 地方做判断处理类似情况
                 *
                 */
                if (data != null) {
                    //上传图片
                    upload(data);
                }
                break;
            case REQUEST_CODE_LOCAL:
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        startPhotoZoom(uri);
                    }
                }
                break;
            default:
                break;
        }
    }
    public static final int REQUEST_CODE_ZOOM = 3;

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
		 * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
		 * 制做的了...吼吼
		 */

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("return-data", true);
        getParent().startActivityForResult(intent, REQUEST_CODE_ZOOM);
    }

    public static final int REQUEST_CODE_LOCAL = 19;

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        getParent().startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    private void upload(Intent data) {
        showProgressDialog(context, "", "头像上传中..", false);
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            String mime = "png";
            XuDecodeBase64 decodeBase64 = new XuDecodeBase64().invoke(photo);
            String pathBase64 = decodeBase64.getPathBase64();
            new Uploadportrait(Config.getCacheID(context), mime, pathBase64, new Uploadportrait.SuccessCallback() {
                @Override
                public void onSuccess() {
                    closeProgressDialog();
                    Log.e("tag", "onSuccess");
                    //Intent i = new Intent(AtyIndCenter.this, AtyShowAll.class);
                    //i.putExtra("isupload", true);
                    //setResult(RESULT_OK, i);
                    showToast("上传成功");
                    getPortraitAndNickName();
                }
            }, new Uploadportrait.FailCallback() {
                @Override
                public void onFail(String error) {
                    closeProgressDialog();
                    showToast("上传失败，请重试");
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return getParent().onKeyDown(keyCode, event);
    }
}
