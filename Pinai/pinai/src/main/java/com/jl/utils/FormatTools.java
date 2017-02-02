package com.jl.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 类描述：FormatTools Bitmap与DrawAble与byte[]与InputStream之间的转换工具类
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-9
 * 下午7:52:46
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FormatTools {
    private static FormatTools tools = new FormatTools();

    public static FormatTools getInstance() {
        if (tools == null) {
            tools = new FormatTools();
            return tools;
        }
        return tools;
    }

    /**
     * 将byte[]转换成InputStream
     *
     * @param b
     * @return InputStream
     * @throws
     * @since 1.0.0
     */
    public InputStream Byte2InputStream(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return bais;
    }

    /**
     * 将InputStream转换成byte[]
     *
     * @param is
     * @return byte[]
     * @throws
     * @since 1.0.0
     */
    public byte[] InputStream2Bytes(InputStream is) {
        String str = "";
        byte[] readByte = new byte[1024];
        try {
            while ((is.read(readByte, 0, 1024)) != -1) {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Bitmap转换成InputStream
     *
     * @param bm
     * @return InputStream
     * @throws
     * @since 1.0.0
     */
    public InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * 将Bitmap转换成InputStream
     *
     * @param bm
     * @param quality
     * @return InputStream
     * @throws
     * @since 1.0.0
     */
    public InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * 将Bitmap转换成InputStream
     *
     * @param is
     * @return Bitmap
     * @throws
     * @since 1.0.0
     */
    public Bitmap InputStream2Bitmap(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    /**
     * 将Bitmap转换成InputStream
     *
     * @param d
     * @return InputStream
     * @throws
     * @since 1.0.0
     */
    public InputStream Drawable2InputStream(Drawable d) {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.Bitmap2InputStream(bitmap);
    }

    /**
     * 将Bitmap转换成InputStream
     *
     * @param is
     * @return Drawable
     * @throws
     * @since 1.0.0
     */
    public Drawable InputStream2Drawable(InputStream is) {
        Bitmap bitmap = this.InputStream2Bitmap(is);
        return this.bitmap2Drawable(bitmap);
    }

    /**
     * 将Bitmap转换成InputStream
     *
     * @param d
     * @return byte[]
     * @throws
     * @since 1.0.0
     */
    public byte[] Drawable2Bytes(Drawable d) {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.Bitmap2Bytes(bitmap);
    }

    /**
     * byte[]转换成Drawable
     *
     * @param b
     * @return Drawable
     * @throws
     * @since 1.0.0
     */
    public Drawable Bytes2Drawable(byte[] b) {
        Bitmap bitmap = this.Bytes2Bitmap(b);
        return this.bitmap2Drawable(bitmap);
    }

    /**
     * byte[]转换成Drawable
     *
     * @param bm
     * @return byte[]
     * @throws
     * @since 1.0.0
     */
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[]转换成Bitmap
     *
     * @param b
     * @return Bitmap
     * @throws
     * @since 1.0.0
     */
    public Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    /**
     * Drawable转换成Bitmap
     *
     * @param drawable
     * @return Bitmap
     * @throws
     * @since 1.0.0
     */
    public Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap转换成Drawable
     *
     * @param bitmap
     * @return Drawable
     * @throws
     * @since 1.0.0
     */
    public Drawable bitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        Drawable d = bd;
        return d;
    }
}