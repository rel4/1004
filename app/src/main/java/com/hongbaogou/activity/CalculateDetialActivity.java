package com.hongbaogou.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.ProgressBarWebView;

/**
 * 计算详情的界面
 */
public class CalculateDetialActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView mButton_Back;
    private ImageView mButton_Refresh;
    
    private ProgressBarWebView webview;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_detial);
        initBarUtils.setSystemBar(this);
        //获取参数
        url = "http://www.qcread.com";
        //name = getIntent().getStringExtra("name");

        //绑定控件
        webview = (ProgressBarWebView) findViewById(R.id.detial_webview);

        //刷新按钮
        mButton_Refresh = (ImageView) findViewById(R.id.img_btn_refresh);
        mButton_Refresh.setOnClickListener(this);

        //后退按钮
        mButton_Back = (ImageView) findViewById(R.id.btn_back);
        mButton_Back.setOnClickListener(this);

        // 设置数据
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        webview.loadUrl(url);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);//在当前webview跳转新的url
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //退回按钮
            case R.id.btn_back:
                finish();
                break;
            //刷新webview界面的按钮
            case R.id.img_btn_refresh:
                webview.loadUrl(url);
                break;
            default:
                break;
        }
    }
}
