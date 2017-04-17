package com.hongbaogou.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.hongbaogou.utils.ToastUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.hongbaogou.R;
import com.hongbaogou.YYJXApplication;
import com.hongbaogou.global.ConstantValues;
import com.hongbaogou.pay.constants.Constants;
import com.hongbaogou.utils.Pref_Utils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;
    private TextView mTxtPayResult;
    private YYJXApplication application;
    private int mPayStatus;

    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        application = (YYJXApplication) getApplication();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(WXPayEntryActivity.this);
        initTitle();
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    private void initTitle() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("支付结果");
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent mBroadIntent = new Intent();
                mBroadIntent.setAction(ConstantValues.ACTION_UPDATA_CART_COUNT);
                mLocalBroadcastManager.sendBroadcast(mBroadIntent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }


    @Override
    public void onResp(BaseResp resp) {
        mTxtPayResult = (TextView) findViewById(R.id.tv_go_home);
        mPayStatus = resp.errCode;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if ((resp.errCode == 0)) {

                mTxtPayResult.setText("支付成功");
                Intent mBroadIntent = new Intent();
                mBroadIntent.setAction(ConstantValues.PAY_SUCCESS);
                mLocalBroadcastManager.sendBroadcast(mBroadIntent);

                String tempmoney = Pref_Utils.getString(WXPayEntryActivity.this, "tempmoney", "0");
                String remainMoney = Pref_Utils.getString(WXPayEntryActivity.this, "money", "0");
                Double double_temp = Double.parseDouble(tempmoney);
                Double double_remain = Double.parseDouble(remainMoney);
                Double money_local = double_temp + double_remain;
                Pref_Utils.putString(WXPayEntryActivity.this, "money", String.valueOf(money_local));
                Pref_Utils.putString(WXPayEntryActivity.this, "tempmoney", "0");
                finish();

            } else if (resp.errCode == -1) {
                mTxtPayResult.setText("支付失败-wx" + resp.errStr);
//                ToastUtil.showToast(this, resp.errStr);
                Pref_Utils.putString(this, "tempmoney", "0");
            } else if (resp.errCode == -2) {
                mTxtPayResult.setText("已取消支付");
                Pref_Utils.putString(this, "tempmoney", "0");
            } else {
                Pref_Utils.putString(this, "tempmoney", "0");
            }
        } else {
            Pref_Utils.putString(this, "tempmoney", "0");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent mBroadIntent = new Intent();
        mBroadIntent.setAction(ConstantValues.ACTION_UPDATA_CART_COUNT);
        mLocalBroadcastManager.sendBroadcast(mBroadIntent);
    }
}