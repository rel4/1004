package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.adapter.WebViewCallBackAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.WebViewCallBackBean;
import com.hongbaogou.bean.WebViewGoolsListBean;
import com.hongbaogou.listener.OnWebViewListener;
import com.hongbaogou.request.WebViewCallBackRequest;
import com.hongbaogou.safewebviewbridge.InjectedChromeClient;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.initBarUtils;

import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends BaseAppCompatActivity implements OnWebViewListener {

    private String title;
    private String url;
    private WebView webview;
    private ProgressBar progress;


    private WebViewCallBackRequest mWebViewCallBackRequest;
    private String uid;
    private ListView mListView;
    private TextView tv_pay_jixu, tv_pay_chakan, tv_more, tv_num_renci, tv_num;
    private Intent mIntent;
    private WebViewCallBackAdapter mAdapter;
    private List<WebViewGoolsListBean> list = new ArrayList<WebViewGoolsListBean>();
    private WebViewCallBackBean mWebViewCallBackBean;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        progress = (ProgressBar) findViewById(R.id.progress);

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

//        //绑定控件
//        webview = (WebView) findViewById(R.id.webView);
//        // 设置数据
//        webview.getSettings().setJavaScriptEnabled(true);
//
        //   webview.setWebChromeClient(new CustomChromeClient("HostApp", HostJsScope.class));
//
////        webview.loadUrl("file:///android_asset/test1.html");
//        webview.loadUrl(url);
//
//        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
//        webview.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);
//                return true;
//            }
//        });

        initBarUtils.setSystemBar(this);


//        webview.loadUrl("file:///android_asset/test1.html");
//        webview.loadUrl(url);

        uid = Pref_Utils.getString(getApplicationContext(), "uid");

      //  mWebViewCallBackRequest.webViewCallBackRequest(uid, this);


        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new WebViewCallBackAdapter(list, this);


        tv_more = (TextView) findViewById(R.id.tv_more);
        tv_pay_jixu = (TextView) findViewById(R.id.tv_pay_jixu);
        tv_pay_chakan = (TextView) findViewById(R.id.tv_pay_chakan);

        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_num_renci = (TextView) findViewById(R.id.tv_num_renci);
        list = (List<WebViewGoolsListBean>) getIntent().getSerializableExtra("data");
        tv_num.setText(intent.getStringExtra("count"));
        tv_num_renci.setText(intent.getStringExtra("zongrenci"));
        mAdapter.addData(list);
        mListView.setAdapter(mAdapter);
    }

    public void back(View v) {
        if (v != null) {
            switch (v.getId()) {
                //查看夺宝记录
                case R.id.tv_pay_chakan:
                    mIntent = new Intent(WebViewActivity.this, SelfBuyRecordActivity.class);
                    startActivity(mIntent);
                    break;
                //继续夺宝
                case R.id.tv_pay_jixu:
                    mIntent = new Intent(WebViewActivity.this, MainActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mIntent.putExtra("position", 0);
                    startActivity(mIntent);
                default:
                    break;
            }
        }
        finish();
    }

    public void reflesh(View v) {

        //     webview.loadUrl(url);

//        mWebViewCallBackRequest.webViewCallBackRequest(uid, this);
    }


    // 设置回退
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
//            webview.goBack(); // goBack()表示返回WebView的上一页面
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    //付款成功的回调
    @Override
    public void OnWebViewListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            if (baseObjectBean.getStatus() == 1) {
                mWebViewCallBackBean = (WebViewCallBackBean) baseObjectBean.getData();
            }
        }
    }

    @Override
    public void OnWebViewListenerFailed(VolleyError error) {

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
                if (progress.getVisibility() == View.GONE) {
                    progress.setVisibility(View.VISIBLE);
                }
                progress.setProgress(newProgress);
            }
        }
    }
}
