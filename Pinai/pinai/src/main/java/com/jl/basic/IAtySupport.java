package com.jl.basic;

import android.content.Context;

/**
 * 类描述：基本类接口 创建人：徐志国 修改人：徐志国 修改时间：2014-11-9 下午7:19:18 修改备注：
 *
 * @version 1.0.0
 */
public interface IAtySupport {
    /**
     * getEimApplication(获取PinApplication.)
     *
     * @return PinApplication
     * @since 1.0.0
     */
    PinApplication getPinApplication();

    /**
     * startService(开启服务) void
     *
     * @since 1.0.0
     */
    void startService();

    /**
     * stopService(终止服务.) void
     *
     * @since 1.0.0
     */
    void stopService();

    /**
     * validateInternet(校验网络-如果没有网络就弹出设置,并返回true.)
     *
     * @return boolean
     * @since 1.0.0
     */
    boolean validateInternet();

    /**
     * hasInternetConnected(校验网络-如果没有网络就返回true.)
     *
     * @return boolean
     * @since 1.0.0
     */
    boolean hasInternetConnected();

    /**
     * isExit(退出应用.) void
     *
     * @since 1.0.0
     */
    void isExit();

    /**
     * hasLocationGPS(判断GPS是否已经开启.)
     *
     * @return boolean
     * @since 1.0.0
     */
    boolean hasLocationGPS();

    /**
     * hasLocationNetWork(判断基站是否已经开启)
     *
     * @return boolean
     * @since 1.0.0
     */
    boolean hasLocationNetWork();

    /**
     * checkMemoryCard(检查内存卡) void
     *
     * @since 1.0.0
     */
    void checkMemoryCard();

    /**
     * showToast(显示Toast,自定义时间)
     *
     * @param text
     * @param longint void
     * @since 1.0.0
     */
    void showToast(String text, int longint);

    /**
     * showToast(短时间显示Toast)
     *
     * @param text void
     * @since 1.0.0
     */
    void showToast(String text);

    /**
     * getContext(获取上下文)
     *
     * @return Context
     * @since 1.0.0
     */
    Context getContext();

    /**
     * 弹出框 void
     *
     * @since 1.0.0
     */
    void openWirelessSet();

    /**
     * showProgressDialog
     *
     * @param title   标题
     * @param message 信息
     * @param isClick 是否可以点击
     * @since 1.0.0
     */
    void showProgressDialog(String title, String message,
                            boolean isClick);


    /**
     * showProgressDialog
     *
     * @param context 上下文
     * @param title   标题
     * @param message 信息
     * @param isClick 是否可以点击 true代表可以点击
     * @since 1.0.0
     */
    void showProgressDialog(Context context, String title, String message,
                            boolean isClick);

    /**
     * 关闭等待框 void
     *
     * @throws
     * @since 1.0.0
     */
    void closeProgressDialog();

    /**
     * 关闭键盘事件 void
     *
     * @throws
     * @since 1.0.0
     */
    void closeInput();
}
