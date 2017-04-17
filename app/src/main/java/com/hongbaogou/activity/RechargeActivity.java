package com.hongbaogou.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.heepay.plugin.activity.Constant;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iapppay.sdk.main.IAppPayOrderUtils;
import com.hongbaogou.R;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.HfbPayInfo;
import com.hongbaogou.bean.RechargeBean;
import com.hongbaogou.bean.RechargeWayBean;
import com.hongbaogou.bean.Result;
import com.hongbaogou.global.ConstantValues;
import com.hongbaogou.listener.OnRechargeListener;
import com.hongbaogou.listener.OnRechargeWayListener;
import com.hongbaogou.pay.AlipayUtils;
import com.hongbaogou.pay.HfbPay;
import com.hongbaogou.pay.PayResult;
import com.hongbaogou.pay.WeiXinPayUtils;
import com.hongbaogou.request.RechargeRequests;
import com.hongbaogou.request.RechargeWayRequests;
import com.hongbaogou.utils.IAppPayOrder;
import com.hongbaogou.utils.PayConfig;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;

import java.util.List;

/**
 * 这是充值的界面
 */
public class RechargeActivity extends BaseNetActivity implements RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener, OnRechargeWayListener, TextWatcher, OnRechargeListener {
    private static final String HFBPAY = "hfbpay";
    private String mMoney;
    private ImageView mImageView;
    private RadioGroup mRadioGroup;
    private EditText met_charge_choose;
    private TextView mtv_recharge_firstpay, mtitle, mTv_personaldata_friend;
    private Button mbtn_charge_choose_a, mbtn_charge_choose_b, mbtn_charge_choose_c, mbtn_charge_choose_d, mbtn_charge_choose_e, mbtn_recharge_ensurepay;
    private String uid;
    private List<RechargeWayBean> mBean;
    private RechargeWayRequests mRequest;

    private String mPay_class, mPay_name, mPay_type;
    private RechargeRequests mRechargeRequests;
    private RechargeBean mRechargeBean;
    private String order_code;
    private String callback_url;
    private boolean hongbao;
    private boolean alipay;
    private boolean wxpay;
    private boolean jdpay;
    private boolean aibeipay;
    private boolean hfbpay;

    private RadioButton rb_weixin;
    private RadioButton rb_jdpay;
    private RadioButton rb_alipay;
    private RadioButton rb_aibei;
    private RadioButton rb_hfb;

    private PayType payType = PayType.NULL;
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hongbao = Pref_Utils.getBoolean(RechargeActivity.this, "hongbao", false);
        alipay = Pref_Utils.getBoolean(RechargeActivity.this, "alipay", false);
        wxpay = Pref_Utils.getBoolean(RechargeActivity.this, "wxpay", false);
        jdpay = Pref_Utils.getBoolean(RechargeActivity.this, "jdpay", false);
        aibeipay = Pref_Utils.getBoolean(RechargeActivity.this, "aibeipay", false);
        hfbpay = Pref_Utils.getBoolean(RechargeActivity.this, "hfbpay", false);


        /**
         * SDK初始化 ，请放在充值启动界面
         */
        IAppPay.init(RechargeActivity.this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, PayConfig.appid);

        setContentView(R.layout.activity_recharge);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        initBarUtils.setSystemBar(this);
        initView();
        initData();
        initBarUtils.setSystemBar(this);


        boolean fromJD = getIntent().getBooleanExtra("fromJD", false);
        if (fromJD) {
            Intent intent = new Intent(RechargeActivity.this, PaySuccessActivity.class);
            startActivity(intent);
            String money = Pref_Utils.getString(RechargeActivity.this, "money");
            if (null != mMoney && !mMoney.isEmpty() && null != money && !money.isEmpty()) {
                Pref_Utils.putString(RechargeActivity.this, "money", (Float.parseFloat(mMoney) + Float.parseFloat(money)) + "");
            }
            Intent mBroadIntent = new Intent();
            mBroadIntent.setAction(ConstantValues.PAY_SUCCESS);
            mLocalBroadcastManager.sendBroadcast(mBroadIntent);
            Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onSuccess(Result result) {
        switch (result.tag) {
            case HFBPAY:
                handleOpenHfb(result.result);
                break;
        }
    }
    private void handleOpenHfb(String result) {
        HfbPayInfo hfbPayInfo = JSON.parseObject(result, HfbPayInfo.class);
        HfbPayInfo.DataEntity data = hfbPayInfo.data;
        HfbPay hfbPay = new HfbPay(RechargeActivity.this);
        hfbPay.pay(data.tokenID, data.agentId, data.billNo, "30");
    }

    /**
     * 接收支付通知结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESULTCODE) {
            String respCode = data.getExtras().getString("respCode");
            String respMessage = data.getExtras().getString("respMessage");
            if (!TextUtils.isEmpty(respCode)) {
                // 支付结果状态（01成功/00处理中/-1 失败）
                if ("01".equals(respCode)) {
                    Intent mBroadIntent = new Intent();
                    mBroadIntent.setAction(ConstantValues.PAY_SUCCESS);
                    mLocalBroadcastManager.sendBroadcast(mBroadIntent);
                    RechargeActivity.this.finish();
                    Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                }
                if ("00".equals(respCode)) {
                    Toast.makeText(getApplicationContext(), "处理中...", Toast.LENGTH_SHORT).show();
                }
                if ("-1".equals(respCode)) {
                    Pref_Utils.putString(this, "tempmoney", "0");
                    Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                }
            }
            // 除支付宝sdk支付respMessage均为null
            if (!TextUtils.isEmpty(respMessage)) {
                // 同步返回的结果必须放置到服务端进行验证, 建议商户依赖异步通知
                PayResult result = new PayResult(respMessage);
                // 同步返回需要验证的信息
                String resultInfo = result.getResult();
                String resultStatus = result.getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    Intent mBroadIntent = new Intent();
                    mBroadIntent.setAction(ConstantValues.PAY_SUCCESS);
                    mLocalBroadcastManager.sendBroadcast(mBroadIntent);
                    RechargeActivity.this.finish();
                    Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                } else {
                    // 判断resultStatus 为非"9000"则代表可能支付失败
                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        Pref_Utils.putString(this, "tempmoney", "0");
                        Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }

    @Override
    public void onError(Result result) {

    }


    private void initData() {
        met_charge_choose.setCursorVisible(false);
        mbtn_charge_choose_a.setOnFocusChangeListener(this);
        mbtn_charge_choose_b.setOnFocusChangeListener(this);
        mbtn_charge_choose_c.setOnFocusChangeListener(this);
        mbtn_charge_choose_d.setOnFocusChangeListener(this);
        mbtn_charge_choose_e.setOnFocusChangeListener(this);
        met_charge_choose.setOnFocusChangeListener(this);

        mRequest = new RechargeWayRequests();
        mRequest.rechargeWayRequests(RechargeActivity.this);
        met_charge_choose.addTextChangedListener(this);

        uid = Pref_Utils.getString(getApplicationContext(), "uid");
        mRechargeRequests = new RechargeRequests();

    }

    private void initView() {
        mtitle = (TextView) findViewById(R.id.tv_recharge_title);
        mtitle.setText(R.string.recharge);

        met_charge_choose = (EditText) findViewById(R.id.et_charge_choose);
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_recharge);
        mImageView = (ImageView) findViewById(R.id.iv_recharge_title);
        mtv_recharge_firstpay = (TextView) findViewById(R.id.tv_recharge_firstpay);
        initPayButton();
        mTv_personaldata_friend = (TextView) findViewById(R.id.tv_personaldata_friend);

        mbtn_charge_choose_a = (Button) findViewById(R.id.btn_charge_choose_a);
        mbtn_charge_choose_b = (Button) findViewById(R.id.btn_charge_choose_b);
        mbtn_charge_choose_c = (Button) findViewById(R.id.btn_charge_choose_c);
        mbtn_charge_choose_d = (Button) findViewById(R.id.btn_charge_choose_d);
        mbtn_charge_choose_e = (Button) findViewById(R.id.btn_charge_choose_e);
        mbtn_recharge_ensurepay = (Button) findViewById(R.id.btn_recharge_ensurepay);


        setFocus(mbtn_charge_choose_a);
        setFocus(mbtn_charge_choose_b);
        setFocus(mbtn_charge_choose_c);
        setFocus(mbtn_charge_choose_d);
        setFocus(mbtn_charge_choose_e);
        //setFocus(met_charge_choose);

        setDefaultBackGround(mbtn_charge_choose_a);
        setDefaultBackGround(mbtn_charge_choose_b);
        setDefaultBackGround(mbtn_charge_choose_c);
        setDefaultBackGround(mbtn_charge_choose_d);
        setDefaultBackGround(mbtn_charge_choose_e);
        setDefaultBackGround(met_charge_choose);
        mRadioGroup.setOnCheckedChangeListener(this);

    }

    private void initPayButton() {
        rb_weixin = ((RadioButton) findViewById(R.id.rb_wxpay));
        rb_alipay = ((RadioButton) findViewById(R.id.rb_alipay));
        rb_jdpay = ((RadioButton) findViewById(R.id.rb_jdpay));
        rb_aibei = ((RadioButton) findViewById(R.id.rb_aibei));
        rb_hfb = ((RadioButton) findViewById(R.id.rb_hfb));

        View wxpay_line = findViewById(R.id.wxpay_line);
        View alipay_line = findViewById(R.id.alipay_line);
        View jdpay_line = findViewById(R.id.jdpay_line);
        View aibei_line = findViewById(R.id.aibei_line);
        View hfb_line = findViewById(R.id.hfb_line);

        if (wxpay) {
            rb_weixin.setVisibility(View.VISIBLE);

            wxpay_line.setVisibility(View.VISIBLE);
            alipay_line.setVisibility(View.GONE);
            jdpay_line.setVisibility(View.GONE);
            aibei_line.setVisibility(View.GONE);
            hfb_line.setVisibility(View.GONE);
        } else {
            rb_weixin.setVisibility(View.GONE);

            wxpay_line.setVisibility(View.GONE);
        }
        if (alipay) {
            rb_alipay.setVisibility(View.VISIBLE);

            wxpay_line.setVisibility(View.GONE);
            alipay_line.setVisibility(View.VISIBLE);
            jdpay_line.setVisibility(View.GONE);
            aibei_line.setVisibility(View.GONE);
            hfb_line.setVisibility(View.GONE);
        } else {
            rb_alipay.setVisibility(View.GONE);

            alipay_line.setVisibility(View.GONE);
        }
        if (jdpay) {
            rb_jdpay.setVisibility(View.VISIBLE);

            wxpay_line.setVisibility(View.GONE);
            alipay_line.setVisibility(View.GONE);
            jdpay_line.setVisibility(View.VISIBLE);
            aibei_line.setVisibility(View.GONE);
            hfb_line.setVisibility(View.GONE);
        } else {
            rb_jdpay.setVisibility(View.GONE);

            jdpay_line.setVisibility(View.GONE);
        }
        if (aibeipay) {
            rb_aibei.setVisibility(View.VISIBLE);

            wxpay_line.setVisibility(View.GONE);
            alipay_line.setVisibility(View.GONE);
            jdpay_line.setVisibility(View.GONE);
            aibei_line.setVisibility(View.VISIBLE);
            hfb_line.setVisibility(View.GONE);
        } else {
            rb_aibei.setVisibility(View.GONE);

            aibei_line.setVisibility(View.GONE);
        }
        if (hfbpay) {
            rb_hfb.setVisibility(View.VISIBLE);

            wxpay_line.setVisibility(View.GONE);
            alipay_line.setVisibility(View.GONE);
            jdpay_line.setVisibility(View.GONE);
            aibei_line.setVisibility(View.GONE);
            hfb_line.setVisibility(View.VISIBLE);
        } else {
            rb_hfb.setVisibility(View.GONE);
            hfb_line.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.iv_recharge_title:
                    closeInputMethod(met_charge_choose);
                    onBackPressed();
                    break;
//                case R.id.rb_recharge_way_other:
//                    RechargeChooseBankDialog.ShowDialog(RechargeActivity.this, mrb_recharge_way_other);
//                    break;
                case R.id.btn_recharge_ensurepay:
                    //确认支付
//                    Intent intent = new Intent(RechargeActivity.this, PayActivity.class);
                    if (Float.parseFloat(mMoney) <= 0f) {
                        ToastUtil.showToast(this, "最少充值1元，请重新输入金额！");
                        return;
                    }else if (Float.parseFloat(mMoney) >= 10000f){
                        ToastUtil.showToast(this, "最多充值1万元，请重新输入金额！");
                        return;
                    } else {
                        if (payType == PayType.WEIXIN) {
                            mRechargeRequests.rechargeRequests(uid, mMoney, "wxpay","charge", this);//参数3为订单生成接口 回调方法：onOrderCodeSuccess
                        } else if (payType == PayType.ALIPAY) {
                            mRechargeRequests.rechargeRequests(uid, mMoney, "alipay","charge", this);//参数3为订单生成接口 回调方法：onOrderCodeSuccess
                        } else if (payType == PayType.JDPAY) {
                            mRechargeRequests.rechargeRequests(uid, mMoney, "jdpay","charge", this);//参数3为订单生成接口 回调方法：onOrderCodeSuccess
                        } else if (payType == PayType.AIBEIPAY) {
                            mRechargeRequests.rechargeRequests(uid, mMoney, "iapppay","charge", this);//参数3为订单生成接口 回调方法：onOrderCodeSuccess
                        } else if (payType == PayType.HFBPAY) {
                            mRechargeRequests.rechargeRequests(uid, mMoney, "heepay","charge", this);//参数3为订单生成接口 回调方法：onOrderCodeSuccess
                        } else {
                            ToastUtil.showToast(RechargeActivity.this, "请选择支付方式");
                        }

                        Pref_Utils.putString(RechargeActivity.this, "tempmoney", mMoney);

                    }
                case R.id.et_charge_choose:
                    // met_charge_choose.requestFocus();
                    met_charge_choose.setCursorVisible(true);
                    // mMoney = met_charge_choose.getText().toString();
                    break;
                case R.id.btn_charge_choose_a:
                    mMoney = mbtn_charge_choose_a.getText().toString();
                    break;
                case R.id.btn_charge_choose_b:
                    mMoney = mbtn_charge_choose_b.getText().toString();
                    break;
                case R.id.btn_charge_choose_c:
                    mMoney = mbtn_charge_choose_c.getText().toString();
                    break;
                case R.id.btn_charge_choose_d:
                    mMoney = mbtn_charge_choose_d.getText().toString();
                    break;
                case R.id.btn_charge_choose_e:
                    mMoney = mbtn_charge_choose_e.getText().toString();
                    break;
                default:
                    break;

            }
        }
    }
    private void closeInputMethod(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void setBackGround(View view) {
        int color = getResources().getColor(R.color.color_shen_red);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, color); // 边框粗细及颜色
        view.setBackgroundDrawable(drawable); // 设置背景（效果就是有边框及底色）
    }

    private void setDefaultBackGround(View view) {
        int color = getResources().getColor(R.color.color_gray_shen);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(2, color);
        view.setBackgroundDrawable(drawable);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_wxpay:
                payType = PayType.WEIXIN;
                break;

            case R.id.rb_alipay:
                payType = PayType.ALIPAY;
                break;

            case R.id.rb_jdpay:
                payType = PayType.JDPAY;
                break;
            case R.id.rb_aibei:
                payType = PayType.AIBEIPAY;
                break;
            case R.id.rb_hfb:
                payType = PayType.HFBPAY;
                break;

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setBackGround(v);
            switch (v.getId()) {
                case R.id.btn_charge_choose_a:
                    mMoney = mbtn_charge_choose_a.getText().toString();
                    break;
                case R.id.btn_charge_choose_b:
                    mMoney = mbtn_charge_choose_b.getText().toString();
                    break;
                case R.id.btn_charge_choose_c:
                    mMoney = mbtn_charge_choose_c.getText().toString();
                    break;
                case R.id.btn_charge_choose_d:
                    mMoney = mbtn_charge_choose_d.getText().toString();
                    break;
                case R.id.btn_charge_choose_e:
                    mMoney = mbtn_charge_choose_e.getText().toString();
                    break;
                case R.id.et_charge_choose:
                    // mMoney = met_charge_choose.getText().toString();
                    break;
                default:
                    break;
            }
        } else {
            setDefaultBackGround(v);
        }
    }

    private void setFocus(View v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
/*        v.requestFocus();
        v.requestFocusFromTouch();*/
    }

    //获取支付方式
    @Override
    public void OnRechargeWayListenerSuccess(BaseListBean baseListBean) {
        if (baseListBean != null) {
            if (baseListBean.getStatus() == 1) {
                mBean = baseListBean.getData();
                for (int i = 0; i < mBean.size(); i++) {
                    RechargeWayBean bean = mBean.get(i);
                    mPay_class = bean.getPay_class();
                    mPay_name = bean.getPay_name();
                    mPay_type = bean.getPay_type();
                }
            }
        }
    }

    //EditText 的监听
    @Override
    public void OnRechargeWayListenerFailed(VolleyError error) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mMoney = met_charge_choose.getText().toString();
    }


    /**
     * 支付结果回调
     */
    IPayResultCallback iPayResultCallback = new IPayResultCallback() {

        @Override
        public void onPayResult(int resultCode, String signvalue, String resultInfo) {
            // TODO Auto-generated method stub
            switch (resultCode) {
                case IAppPay.PAY_SUCCESS:
                    //调用 IAppPayOrderUtils 的验签方法进行支付结果验证
                    boolean payState = IAppPayOrderUtils.checkPayResult(signvalue, PayConfig.publicKey);
                    if (payState) {
                        Intent intent = new Intent(RechargeActivity.this, PaySuccessActivity.class);
                        startActivity(intent);
                        String money = Pref_Utils.getString(RechargeActivity.this, "money");
                        Pref_Utils.putString(RechargeActivity.this,"money",(Float.parseFloat(mMoney)+Float.parseFloat(money))+"");
                        Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                    }
                    break;
                case IAppPay.PAY_ING:
                    Toast.makeText(RechargeActivity.this, "成功下单", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(RechargeActivity.this, resultInfo, Toast.LENGTH_LONG).show();
                    break;
            }
            Log.e("TAG", "requestCode:" + resultCode + ",signvalue:" + signvalue + ",resultInfo:" + resultInfo);
        }
    };

    //生成订单
    @Override
    public void OnRechargeListenerSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean != null) {
            if (baseObjectBean.getStatus() == 1) {
                mRechargeBean = (RechargeBean) baseObjectBean.getData();
                order_code = mRechargeBean.getOrder_code();
                callback_url = mRechargeBean.getCallback_url();

                switch (payType) {
                    case WEIXIN:
                        WeiXinPayUtils weiXinPayUtils = new WeiXinPayUtils(RechargeActivity.this,mMoney, order_code);//拉起微信支付
                        break;
                    case ALIPAY:
                        AlipayUtils alipayUtils = new AlipayUtils(RechargeActivity.this);
                        alipayUtils.pay(mMoney, order_code);
                        break;
                    case JDPAY:
                        startActivity(new Intent(RechargeActivity.this, PayResultShowActivity.class).putExtra("MONEY", Float.parseFloat(mMoney)).putExtra("ORDER_CODE", order_code).putExtra("CALLBACK_URL", callback_url).putExtra("fromCharge", true));
                        RechargeActivity.this.finish();
                        break;
                    case AIBEIPAY:
                        IAppPayOrder.startPay(this, Float.parseFloat(mMoney), order_code, callback_url, iPayResultCallback);
                        break;
                    case HFBPAY:
                        request.get(HFBPAY, "cart/heepay?", "&pay_type=30" + "&pay_amt=" + mMoney + "&code=" + order_code);
                        break;
                }

                Pref_Utils.putString(this, "tempmoney", String.valueOf(mMoney));

            } else {
                ToastUtil.showToast(RechargeActivity.this, "生成订单失败");
            }
        }
    }

    @Override
    public void OnRechargeListenerFailed(VolleyError error) {

    }
    enum PayType {
        NULL,WEIXIN, ALIPAY, JDPAY, AIBEIPAY, HFBPAY;
    }
}
