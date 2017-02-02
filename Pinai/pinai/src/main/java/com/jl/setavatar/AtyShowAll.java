package com.jl.setavatar;

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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jl.atys.individualcenter.AtyIndCenter;
import com.jl.atys.individualcenter.AtyIndCheckOutMy;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hrb.jl.pinai.R;

/**
 * 类名称：AtyShowAll
 * 类描述： 显示所有头像
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2015-1-6 下午8:00:32
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyShowAll extends AtySupport {
    private final static int SCAN_OK = 1;
    private int from;//从哪个Activity请求的
    private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
    private List<ImageBean> list = new ArrayList<ImageBean>();
    private ProgressDialog mProgressDialog;
    private ShowAllAdapter adapter;
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
                        adapter = new ShowAllAdapter(AtyShowAll.this, list, mGroupGridView);
                        mGroupGridView.setAdapter(adapter);
                        showLayout.setVisibility(View.GONE);
                    }
                    break;
            }
        }

    };
    private LinearLayout showLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_show_all);
        mGroupGridView = (GridView) findViewById(R.id.main_grid);
        from = getIntent().getExtras().getInt("from");
        getImages();
        mGroupGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                List<String> childList = mGruopMap.get(list.get(position).getFolderName());
                Intent mIntent = new Intent(AtyShowAll.this, AtyShowImage.class);
                mIntent.putStringArrayListExtra("data", (ArrayList<String>) childList);
                startActivityForResult(mIntent, 0);
            }
        });
        showLayout = (LinearLayout) findViewById(R.id.show_layout);
    }

    /**
     * 解释一下原因，这里是这么个传递顺序   AtyIndCenter ---> AtyShowAll  ---> AtyShowImage
     * 其中 AtyIndCenter 又因为在Tabhost里面，他需要从父容器获得到onActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (from) {
            //从个人中心传入
            case Config.KEY_FROM_CENTER:
                Intent ai = new Intent(AtyShowAll.this, AtyIndCenter.class);
                try {
                    boolean isupload = (boolean) data.getExtras().get("isupload");
                    if (isupload) {
                        setResult(RESULT_OK, ai);
                        finish();
                    }
                } catch (Exception ignored) {

                }
                break;
            //从个人资料传入
            case Config.KEY_FROM_CHECK_MY:
                Intent i = new Intent(AtyShowAll.this, AtyIndCheckOutMy.class);
                setResult(RESULT_OK, i);
                finish();
                break;
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
                ContentResolver mContentResolver = AtyShowAll.this.getContentResolver();
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

        for (Map.Entry<String, List<String>> entry : mGruopMap.entrySet()) {
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
