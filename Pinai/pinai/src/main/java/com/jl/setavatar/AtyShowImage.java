/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jl.setavatar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.net.Uploadportrait;
import com.jl.utils.XuDecodeBase64;

import java.io.File;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类名称：AtyShowImage
 * 类描述：显示大图
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2015-1-6 下午8:00:51
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyShowImage extends AtySupport {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_show);
        init();
    }

    private void init() {
        GridView mGridView = (GridView) findViewById(R.id.child_grid);
        List<String> list = getIntent().getStringArrayListExtra("data");
        ShowAdpter adapter = new ShowAdpter(this, list, mGridView);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String path = (String) view.getTag();
                ShowAdpter.ViewHolder viewHolder = (ShowAdpter.ViewHolder) view.getTag();
                String path = (String) viewHolder.mImageView.getTag();
                Log.e("Tag", path);
                File file = new File(path);
                Uri fileUri = Uri.fromFile(file);
                startPhotoZoom(fileUri);
            }
        });
    }

    public void back(View v) {

        finish();
    }

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
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            // 取得裁剪后的图片
            case 3:
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
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    Intent i = new Intent(AtyShowImage.this, AtyShowAll.class);
                    i.putExtra("isupload", true);
                    setResult(RESULT_OK, i);
                    finish();
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
}
