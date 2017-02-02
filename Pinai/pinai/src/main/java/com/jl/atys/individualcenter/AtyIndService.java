package com.jl.atys.individualcenter;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.jl.basic.AtySupport;

import hrb.jl.pinai.R;

/**
 * 类描述：服务条款
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-30 下午2:14:01
 * 修改备注：
 *
 * @version 1.0.0
 */

public class AtyIndService extends AtySupport {
    private WebView webView;
    private TextView titleBar;
    private String url;//地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_ind_service);
        url = getIntent().getExtras().getString("url");
        titleBar = (TextView) findViewById(R.id.comment_title);

        init();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.service_webview);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleBar.setText(title);
                super.onReceivedTitle(view, title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

    }

    public void back(View v) {
        finish();
    }
}
