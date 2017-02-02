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
package com.jl.atys.gopin;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jl.basic.AtySupport;
import com.jl.utils.UserTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import hrb.jl.pinai.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 类名称：AtyShowPortrait
 * 类描述：显示头像大图
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2015-2-3 下午8:00:51
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyShowPortrait extends AtySupport {
    private PhotoViewAttacher mAttacher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_show_portrait);
        //这里根据屏幕大小来调节显示框大小
        ImageView mImageView = (ImageView) findViewById(R.id.iv_photo);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels - 20;
        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
        params.height = width;
        params.width = width;
        mImageView.setLayoutParams(params);
        String name = (String) getIntent().getExtras().get("name");
        UserTools.displayImage(name, mImageView, getOptions());
        mAttacher = new PhotoViewAttacher(mImageView);
    }

    public void back(View v) {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAttacher.cleanup();
    }

    public DisplayImageOptions getOptions() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.unphoto)           //加载图片时的图片
                .showImageForEmptyUri(R.drawable.unphoto)         //没有图片资源时的默认图片
                .showImageOnFail(R.drawable.unphoto)              //加载失败时的图片
                .cacheInMemory(true)                                  //启用内存缓存
                .cacheOnDisk(true)                                    //启用外存缓存
                .considerExifParams(true)                             //启用EXIF和JPEG图像格式
                .build();
        return options;
    }
}
