package com.hongbaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hongbaogou.R;
import com.hongbaogou.activity.base.BaseAppCompatActivity;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.RechargeBean;
import com.hongbaogou.listener.OnRechargeListener;
import com.hongbaogou.request.RechargeRequests;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.initBarUtils;

public class PayActivity extends BaseAppCompatActivity implements OnRechargeListener {
    private Button mbutton;
    private TextView text;
    private RechargeRequests mRequest;
    private String uid;
    private String mMoney;
    private String mPay_class;
    private RechargeBean mBean;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initBarUtils.setSystemBar(this);
        initView();
        initData();
        initBarUtils.setSystemBar(this);

    }

    private void initData() {
        Intent intent = getIntent();
        mMoney = intent.getStringExtra("money");
        mPay_class = intent.getStringExtra("pay_class");
        text.setText("金额：" + mMoney + "方式" + mPay_class);
        //System.out.println("=========money===================>" + money);
        // System.out.println("=========pay_class===================>" + pay_class);
        mRequest = new RechargeRequests();
        uid = Pref_Utils.getString(getApplicationContext(), "uid");
    }

    private void initView() {
        mbutton = (Button) findViewById(R.id.btn_pay);
        text = (TextView) findViewById(R.id.text);
        mImageView = (ImageView) findViewById(R.id.iv_recharge_title);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_recharge_title:
                finish();
                break;
            case R.id.btn_pay:
                mRequest.rechargeRequests(uid, mMoney,"wxpay","charge", this);
                break;
            default:
                break;
        }
        //BaseUtils.colseSoftKeyboard(this);
    }

    //充值订单生成
    @Override
    public void OnRechargeListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            if (baseObjectBean.getStatus() == 1) {
                mBean = (RechargeBean) baseObjectBean.getData();
                String order_code = mBean.getOrder_code();
                String callback_url = mBean.getCallback_url();
                //// TODO: 2015/12/16  判断 callback_url （APP端支付成功后的异步回调地址）是否成功
                if (true) {
                    Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void OnRechargeListenerFailed(VolleyError error) {

    }
}
