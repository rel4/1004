package com.hongbaogou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;

public class PayResultShowActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    String pre_url = "https://m.jdpay.com/wepay/web/pay/confirm";
    private String pay_callback_url;
    private boolean fromOrder;
    private boolean fromCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result_show);
        Intent intent = getIntent();
        int payMoneys = (int) intent.getFloatExtra("MONEY", 0);
        String order_code = intent.getStringExtra("ORDER_CODE");
//        String pay_callback_url = intent.getStringExtra("CALLBACK_URL");
        fromOrder = intent.getBooleanExtra("fromOrder", false);
        fromCharge = intent.getBooleanExtra("fromCharge", false);
        initTitleBar();
        pay_callback_url = "http://1yzs.cn/index.php/pay/jdpay_test/testpay?code=" + order_code + "&money=" + payMoneys;

        initData(pay_callback_url);
    }

    private void initTitleBar() {

        iv_back = ((ImageView) findViewById(R.id.iv_back));
        iv_back.setOnClickListener(this);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("支付结果");
    }

    private void initData(final String pay_callback_url) {
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(pay_callback_url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (pre_url.equals(url)) {
                    if(fromOrder){
                        startActivity(new Intent(PayResultShowActivity.this, PayOrderActivity.class).putExtra("fromJD",true));
                        PayResultShowActivity.this.finish();
                    }

                    if(fromCharge){
                        startActivity(new Intent(PayResultShowActivity.this, RechargeActivity.class).putExtra("fromJD",true));
                        PayResultShowActivity.this.finish();
                    }


                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
