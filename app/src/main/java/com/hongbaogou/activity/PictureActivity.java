package com.hongbaogou.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.ProgressBarWebView;

/*
 * 图文详情
 */
public class PictureActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ProgressBarWebView webview;
    private String url;
    private TextView title;
    //刷新和回退
    private ImageView mButton_Back;
    private ImageView mButton_Refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        url = getIntent().getStringExtra("url");

        findAllView();

        initWebView();

        initBarUtils.setSystemBar(this);
    }

    private void initWebView() {
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
                webview.loadUrl(url);
                return true;
            }
        });
    }

    //初始化view
    private void findAllView() {
        webview = (ProgressBarWebView) findViewById(R.id.tuwen_webview);
        title = (TextView) findViewById(R.id.title);
        title.setText("图文详情");

        mButton_Back = (ImageView) findViewById(R.id.btn_back);
        mButton_Back.setOnClickListener(this);

        mButton_Refresh = (ImageView) findViewById(R.id.menuItem);
        mButton_Refresh.setImageResource(R.mipmap.btn_refresh);
        mButton_Refresh.setVisibility(View.VISIBLE);
        mButton_Refresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //退回按钮
            case R.id.btn_back:
                finish();
                break;
            //刷新webview界面的按钮
            case R.id.menuItem:
                webview.loadUrl(url);
                break;
            default:
                break;
        }
    }
}
