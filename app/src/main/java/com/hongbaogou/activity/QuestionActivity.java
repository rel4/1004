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
import com.hongbaogou.view.ProgressBarWebView;

public class QuestionActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView mButton_Back;
    private ImageView mButton_Refresh;
    private TextView title;
    private ProgressBarWebView webview;
    private String url;
    //private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.problem);

        //获取参数
        url = getIntent().getStringExtra("url");
        //name = getIntent().getStringExtra("name");

        //绑定控件
        webview = (ProgressBarWebView) findViewById(R.id.question_webview);

        //刷新按钮
        mButton_Refresh = (ImageView) findViewById(R.id.menuItem);
        mButton_Refresh.setImageResource(R.mipmap.btn_refresh);
        mButton_Refresh.setVisibility(View.VISIBLE);
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
            case R.id.menuItem:
                webview.loadUrl(url);
                break;
            default:
                break;
        }
    }
}
