package com.jl.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserTools {
    /**
     * 将图片截取为圆角图片
     *
     * @param bitmap 原图片
     * @param ratio  截取比例，如果是8，则圆角半径是宽高的1/8，如果是2，则是圆形图片
     * @return 圆角矩形图片
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float ratio) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, bitmap.getWidth() / ratio,
                bitmap.getHeight() / ratio, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 加载图片
     *
     * @param uri       地址
     * @param imageView 图片容器
     */
    public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (options != null) {
            imageLoader.displayImage(uri, imageView, options);
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageLoader.displayImage(uri, imageView);
        }
    }

    /**
     * 加载图片
     *
     * @param uri 地址
     */
    public static Bitmap loadImageSync(String uri) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        return imageLoader.loadImageSync(uri);
    }

    public static void displayImageHasListener(String uri, final ImageView imageView, DisplayImageOptions options) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(uri, imageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                view.setLayoutParams(new LinearLayout.LayoutParams(loadedImage.getWidth() / 2, loadedImage.getHeight() / 2));
            }
        });
    }

    public static void displayImageHasListener(String uri, final ImageView imageView, DisplayImageOptions options, final int height, final int width) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(uri, imageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //super.onLoadingComplete(imageUri, view, loadedImage);
                FormatTools ft = new FormatTools();
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(null);
                imageView.setBackgroundDrawable(ft.bitmap2Drawable(loadedImage));
            }
        });
    }


    /**
     * 判定输入汉字
     *
     * @param c 字符
     * @return boolean
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 检测String是否全是中文
     *
     * @param name 文字
     * @return boolean
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * @param bitmap 图片
     * @return
     */
    public static String imgToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return "解析失败";
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * decoderBase64File:(将base64字符解码保存文件). <br/>
     *
     * @param base64Code 编码后的字串
     * @param savePath   文件保存路径
     * @since JDK 1.6
     */
    public static void decoderBase64File(String base64Code, String savePath) {
        //byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(savePath);
            out.write(buffer);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 友盟获取设备识别id
     *
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得标题栏高度
     *
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
//        int statusHeight = 0;
//        Rect localRect = new Rect();
//        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
//        statusHeight = localRect.top;
//        if (0 == statusHeight){
//            Class<?> localClass;
//            try {
//                localClass = Class.forName("com.android.internal.R$dimen");
//                Object localObject = localClass.newInstance();
//                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
//                statusHeight = activity.getResources().getDimensionPixelSize(i5);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        // decorView是window中的最顶层view，可以从window中获取到decorView，然后decorView有个getWindowVisibleDisplayFrame方法可以获取到程序显示的区域，包括标题栏，但不包括状态栏。
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        System.out.println(statusBarHeight + "---");
        return statusBarHeight;
    }

    /**
     * 获得当前的视图
     *
     * @param activity
     * @return
     */
    // 获取指定Activity的截屏，保存到png文件
    public static Bitmap takeScreenShot(Activity activity) {

        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;

        //
        int contentTop = activity.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - statusBarHeight;
        int cutHeight = titleBarHeight + statusBarHeight;

        int bHeight = b1.getHeight();
        int nHeight = height - cutHeight;
        if ((cutHeight + nHeight) > bHeight) {
            nHeight = bHeight - cutHeight;
        }

        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, cutHeight, width, nHeight);
        view.destroyDrawingCache();
        //问题
//       if (y + height > source.getHeight()) {
//              throw new IllegalArgumentException("y + height must be <= bitmap.height()");
//          }
        return b;
    }


    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

}
