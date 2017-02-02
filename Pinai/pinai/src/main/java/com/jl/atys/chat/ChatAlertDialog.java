/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jl.atys.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.util.ImageUtils;
import com.jl.atys.chat.task.DownloadImageTask;
import com.jl.atys.chat.utils.ImageCache;
import com.jl.basic.AtySupport;

import java.io.File;

import hrb.jl.pinai.R;

/**
 * 对话框
 */
public class ChatAlertDialog extends AtySupport {
    private int position;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_alert_dialog);
        TextView mTextView = (TextView) findViewById(R.id.title);
        Button mButton = (Button) findViewById(R.id.btn_cancel);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        editText = (EditText) findViewById(R.id.edit);
        //提示内容
        String msg = getIntent().getStringExtra("msg");
        //提示标题
        String title = getIntent().getStringExtra("title");
        //voicePath = getIntent().getStringExtra("voicePath");
        position = getIntent().getIntExtra("position", -1);
        //是否显示取消标题
        boolean isCanceTitle = getIntent().getBooleanExtra("titleIsCancel", false);
        //是否显示取消按钮
        boolean isCanceShow = getIntent().getBooleanExtra("cancel", false);
        //是否显示文本编辑框
        boolean isEditextShow = getIntent().getBooleanExtra("editTextShow", false);
        //转发复制的图片的path
        String path = getIntent().getStringExtra("forwardImage");
        if (msg != null)
            ((TextView) findViewById(R.id.alert_message)).setText(msg);
        if (title != null)
            mTextView.setText(title);
        if (isCanceTitle) {
            mTextView.setVisibility(View.GONE);
        }
        if (isCanceShow)
            mButton.setVisibility(View.VISIBLE);
        if (path != null) {
            //优先拿大图，没有去取缩略图
            if (!new File(path).exists())
                path = DownloadImageTask.getThumbnailImagePath(path);
            imageView.setVisibility(View.VISIBLE);
            (findViewById(R.id.alert_message)).setVisibility(View.GONE);
            if (ImageCache.getInstance().get(path) != null) {
                imageView.setImageBitmap(ImageCache.getInstance().get(path));
            } else {
                Bitmap bm = ImageUtils.decodeScaleImage(path, 150, 150);
                imageView.setImageBitmap(bm);
                ImageCache.getInstance().put(path, bm);
            }

        }
        if (isEditextShow) {
            editText.setVisibility(View.VISIBLE);

        }
    }

    public void ok(View view) {
        setResult(RESULT_OK, new Intent().putExtra("position", position).
                        putExtra("edittext", editText.getText().toString())
                /*.putExtra("voicePath", voicePath)*/);
        if (position != -1)
            AtyChat.resendPos = position;
        finish();

    }

    public void cancel(View view) {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }


}
