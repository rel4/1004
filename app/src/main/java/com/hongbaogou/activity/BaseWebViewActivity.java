package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.safewebviewbridge.HostJsScope;
import com.hongbaogou.safewebviewbridge.InjectedChromeClient;
import com.hongbaogou.utils.initBarUtils;

public class BaseWebViewActivity extends BaseAppCompatActivity {

    private String title;
    private String url;
    private WebView webview;
    private ProgressBar progress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web_view);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");

        progress = (ProgressBar) findViewById(R.id.progress);

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        //绑定控件
        webview = (WebView) findViewById(R.id.webView);
        // 设置数据
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview.setWebChromeClient(new CustomChromeClient("HostApp", HostJsScope.class));

//        webview.loadUrl("file:///android_asset/test1.html");

        System.out.println("-------webview----------"+url);

        webview.loadUrl(url);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        initBarUtils.setSystemBar(this);

    }

    public void back(View v) {
        finish();
    }

    public void reflesh(View v) {
        webview.loadUrl(url);
    }


    // 设置回退
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    public class CustomChromeClient extends InjectedChromeClient {

        public CustomChromeClient(String injectedName, Class injectedCls) {
            super(injectedName, injectedCls);
        }

        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                progress.setVisibility(View.GONE);
            } else {
                if(progress.getVisibility() == View.GONE){
                    progress.setVisibility(View.VISIBLE);
                }
                progress.setProgress(newProgress);
            }

        }

    }

}
