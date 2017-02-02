package com.jl.opengallery;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jl.basic.AtySupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import hrb.jl.pinai.R;

/**
 * 类名称：AtyOpenPic
 * 类描述： 打开本地图片预览
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2015-1-9 下午1:07:10
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyOpenPic extends AtySupport {
    private final static int SCAN_OK = 1;
    private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
    private List<ImageBean> list = new ArrayList<ImageBean>();
    private ProgressDialog mProgressDialog;
    private PicAdapter adapter;
    private GridView mGroupGridView;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    //关闭进度条
                    mProgressDialog.dismiss();
                    list = subGroupOfImage(mGruopMap);
                    if (list != null) {
                        adapter = new PicAdapter(AtyOpenPic.this, list, mGroupGridView);
                        mGroupGridView.setAdapter(adapter);
                        showLayout.setVisibility(View.GONE);
                    } else {

                    }


                    break;
            }
        }

    };
    private LinearLayout showLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_open_pic);
        mGroupGridView = (GridView) findViewById(R.id.main_grid);
        getImages();
        mGroupGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                List<String> childList = mGruopMap.get(list.get(position).getFolderName());
                Intent mIntent = new Intent(AtyOpenPic.this, AtyOpenPicItem.class);
                mIntent.putStringArrayListExtra("data", (ArrayList<String>) childList);
                startActivityForResult(mIntent, 0);
            }
        });
        showLayout = (LinearLayout) findViewById(R.id.show_layout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<String> pathAll = data.getExtras().getStringArrayList("pathAll");
            Intent i = new Intent(AtyOpenPic.this, getIntent().getClass());
            try {
                boolean isok = (boolean) data.getExtras().get("isok");
                if (isok) {
                    i.putStringArrayListExtra("pathAll", (ArrayList) pathAll);
                    setResult(RESULT_OK, i);
                    finish();
                }
            } catch (Exception ignored) {
            }

        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        //显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = AtyOpenPic.this.getContentResolver();
                //只查询jpeg和png和jpg的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    //获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getName();
                    //根据父路径名将图片放入到mGruopMap中
                    if (!mGruopMap.containsKey(parentName)) {
                        List<String> chileList = new ArrayList<String>();
                        chileList.add(path);
                        mGruopMap.put(parentName, chileList);
                    } else {
                        mGruopMap.get(parentName).add(path);
                    }
                }

                mCursor.close();

                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(SCAN_OK);

            }
        }).start();

    }


    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     *
     * @param mGruopMap
     * @return
     */
    private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap) {
        if (mGruopMap.size() == 0) {
            return null;
        }
        List<ImageBean> list = new ArrayList<ImageBean>();

        Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            ImageBean mImageBean = new ImageBean();
            String key = entry.getKey();
            List<String> value = entry.getValue();

            mImageBean.setFolderName(key);
            mImageBean.setImageCounts(value.size());
            mImageBean.setTopImagePath(value.get(0));//获取该组的第一张图片

            list.add(mImageBean);
        }

        return list;

    }

    public void back(View v) {
        finish();
    }

}
