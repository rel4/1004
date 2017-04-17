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

//设置中关于界面
public class AboutActivity extends BaseAppCompatActivity {
    private ProgressBarWebView mWebView;
    private ImageView mIv_about_back, mIv_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initBarUtils.setSystemBar(this);

        initView();
        initData();
        initBarUtils.setSystemBar(this);
    }
    private void initData() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);//在当前webview跳转新的url
                return true;
            }
        });
        mWebView.loadUrl("http://www.iqiyi.com/");
    }

    private void initView() {
        mWebView = (ProgressBarWebView) findViewById(R.id.webview_about);
        mIv_about_back = (ImageView) findViewById(R.id.iv_about_back);
        mIv_refresh = (ImageView) findViewById(R.id.iv_refresh);

    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.iv_about_back:
                    finish();
                    break;
                case R.id.iv_refresh:
                    mWebView.loadUrl("http://v2.qcread.com/");
                default:
                    break;
            }
        }
    }
}
