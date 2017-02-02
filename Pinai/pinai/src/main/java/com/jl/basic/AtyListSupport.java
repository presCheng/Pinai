package com.jl.basic;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jl.customs.CustomProgressDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import hrb.jl.pinai.R;

/**
 * 类描述：基本ListActivity
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-9
 * 下午7:15:09 修改备注：
 *
 * @version 1.0.0
 */
public class AtyListSupport extends ListActivity implements IAtySupport {
    protected Context context = null;
    protected PinApplication pinApplication;
    protected CustomProgressDialog progressDialog = null;
    protected DisplayImageOptions options;//图片设置

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * @param savedInstanceState
     * @since 1.0.0
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        pinApplication = (PinApplication) getApplication();
        pinApplication.addActivity(this);
        setImageOptions();
    }

    /**
     * 图片加载配置
     *
     * @return DisplayImageOptions
     */
    public DisplayImageOptions getOptions() {
        return options;
    }

    /**
     * 设置图片加载配置
     */
    public void setImageOptions() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.unphoto)           //加载图片时的图片
                .showImageForEmptyUri(R.drawable.unphoto)         //没有图片资源时的默认图片
                .showImageOnFail(R.drawable.unphoto)              //加载失败时的图片
                .cacheInMemory(true)                                  //启用内存缓存
                .cacheOnDisk(true)                                    //启用外存缓存
                .considerExifParams(true)                             //启用EXIF和JPEG图像格式
                .displayer(new RoundedBitmapDisplayer(100))           //设置显示风格这里是圆角矩形
                .build();
    }

    @Override
    public PinApplication getPinApplication() {
        return pinApplication;
    }

    @Override
    public void startService() {

    }

    @Override
    public void stopService() {

    }

    @Override
    public boolean validateInternet() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            openWirelessSet();
            return false;
        } else {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        openWirelessSet();
        return false;
    }

    @Override
    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void isExit() {
        new AlertDialog.Builder(context).setTitle("确定退出吗?")
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopService();
                        pinApplication.exit();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public boolean hasLocationGPS() {
        LocationManager manager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return manager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public boolean hasLocationNetWork() {
        LocationManager manager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return manager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void checkMemoryCard() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("请检查内存卡")
                    .setPositiveButton("设置",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    Intent intent = new Intent(
                                            Settings.ACTION_SETTINGS);
                                    context.startActivity(intent);
                                }
                            })
                    .setNegativeButton("退出",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    pinApplication.exit();
                                }
                            }).create().show();
        }
    }

    @Override
    public void showToast(String text, int longint) {
        Toast.makeText(context, text, longint).show();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void openWirelessSet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示")
                .setMessage("请检查网络连接")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        pinApplication.exit();
                    }
                });
        builder.create().show();
    }

    @Override
    public void showProgressDialog(String title, String message, boolean isClick) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(context);
            progressDialog.setTitile(title);
            progressDialog.setCancelable(isClick);// 设置不可点击
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    /**
     * showProgressDialog
     *
     * @param context 上下文
     * @param title   标题
     * @param message 信息
     * @param isClick 是否可以点击 false代表可以点击
     * @since 1.0.0
     */
    @Override
    public void showProgressDialog(Context context, String title, String message, boolean isClick) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setTitile(title);
            progressDialog.setCancelable(isClick);// 设置不可点击
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
