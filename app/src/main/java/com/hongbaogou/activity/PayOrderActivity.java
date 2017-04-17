package com.hongbaogou.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.heepay.plugin.activity.Constant;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iapppay.sdk.main.IAppPayOrderUtils;
import com.hongbaogou.R;
import com.hongbaogou.adapter.OrderAdapter;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.HfbPayInfo;
import com.hongbaogou.bean.PayCodeBean;
import com.hongbaogou.bean.PayOrderBean;
import com.hongbaogou.bean.PayOrderGoodsBean;
import com.hongbaogou.bean.Result;
import com.hongbaogou.bean.WebViewCallBackBean;
import com.hongbaogou.bean.WebViewGoolsListBean;
import com.hongbaogou.global.ConstantValues;
import com.hongbaogou.listener.OnOrderCodeListener;
import com.hongbaogou.listener.OnPayByHCListener;
import com.hongbaogou.listener.OnPayListener;
import com.hongbaogou.listener.OnPayOrderListener;
import com.hongbaogou.pay.AlipayUtils;
import com.hongbaogou.pay.HfbPay;
import com.hongbaogou.pay.PayResult;
import com.hongbaogou.pay.WeiXinPayUtils;
import com.hongbaogou.request.OrderCodeRequest;
import com.hongbaogou.request.PayOrderRequest;
import com.hongbaogou.request.PayRequest;
import com.hongbaogou.utils.IAppPayOrder;
import com.hongbaogou.utils.PayConfig;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.utils.initBarUtils;
import com.hongbaogou.view.NetErrorView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PayOrderActivity extends BaseNetActivity implements OnPayOrderListener, NetErrorView.OnReloadListener, OnPayListener, OnOrderCodeListener, OnPayByHCListener {

    private static final String HFBPAY = "hfbpay";
    private PayOrderBean payOrderBean;
    private ListView orderList;
    private OrderAdapter orderAdapter;
    private RadioGroup radioGroup;

    private RadioButton rb_weixin;
    private RadioButton rb_jdpay;
    private RadioButton rb_alipay;
    private RadioButton rb_aibei;
    private RadioButton rb_hfb;

    private RelativeLayout pay_hint;
    private boolean payWay = false;
    private boolean isOrderShow = true;
    private PayOrderRequest payOrderRequest;
    private PayRequest payRequest;
    private NetErrorView netErrorView;
    private ScrollView scrollView;
    //支付的金额对象
    private TextView tv_totalpay;
    //用户账户金额对象
    private TextView remainderMoney;
    private TextView money;
    private CheckBox checkbox;
    private String uid;
    private String info;
    private ProgressDialog dialog;

    private PayType payType = PayType.NULL;
    private PayReceiver payReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;
    private boolean hongbao;
    private boolean alipay;
    private boolean wxpay;
    private boolean jdpay;
    private boolean aibeipay;
    private boolean hfbpay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hongbao = Pref_Utils.getBoolean(PayOrderActivity.this, "hongbao", false);
        alipay = Pref_Utils.getBoolean(PayOrderActivity.this, "alipay", false);
        wxpay = Pref_Utils.getBoolean(PayOrderActivity.this, "wxpay", false);
        jdpay = Pref_Utils.getBoolean(PayOrderActivity.this, "jdpay", false);
        aibeipay = Pref_Utils.getBoolean(PayOrderActivity.this, "aibeipay", false);
        hfbpay = Pref_Utils.getBoolean(PayOrderActivity.this, "hfbpay", false);

        /**
         * SDK初始化 ，请放在游戏启动界面
         */
        IAppPay.init(PayOrderActivity.this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, PayConfig.appid);

        setContentView(R.layout.activity_pay_order);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        initBarUtils.setSystemBar(this);
        initView();

        boolean fromJD = getIntent().getBooleanExtra("fromJD", false);
        if (fromJD) {
            payBuyAccountMoney();
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantValues.PAY_SUCCESS);
        payReceiver = new PayReceiver();
        mLocalBroadcastManager.registerReceiver(payReceiver, intentFilter);
    }

    @Override
    public void onSuccess(Result result) {
        switch (result.tag) {
            //汇付宝所需支付参数请求成功（tokenID; agentId; billNo;）
            case HFBPAY:
                handleOpenHfb(result.result);
                break;
        }
    }
    private void handleOpenHfb(String  result) {
        HfbPayInfo hfbPayInfo = JSON.parseObject(result, HfbPayInfo.class);
        HfbPayInfo.DataEntity data = hfbPayInfo.data;
        HfbPay hfbPay = new HfbPay(PayOrderActivity.this);
        hfbPay.pay(data.tokenID, data.agentId, data.billNo, "30");
    }

    /**
     * 接收汇付宝支付通知结果
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
                    Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                    payBuyAccountMoney();
                }
                if ("00".equals(respCode)) {
                    Toast.makeText(getApplicationContext(), "处理中...", Toast.LENGTH_SHORT).show();
                }
                if ("-1".equals(respCode)) {
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
                    Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                } else {
                    // 判断resultStatus 为非"9000"则代表可能支付失败
                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        Toast.makeText(this, "支付失败2", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }
    @Override
    public void onError(Result result) {

    }

    class PayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (ConstantValues.PAY_SUCCESS.equals(intent.getAction())) {
                    payBuyAccountMoney();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        mLocalBroadcastManager.unregisterReceiver(payReceiver);
        super.onDestroy();
    }

    private void initView() {

        uid = Pref_Utils.getString(this, "uid");
        Intent intent = getIntent();
        info = intent.getStringExtra("info");

        money = (TextView) findViewById(R.id.money);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        netErrorView = (NetErrorView) findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tv_totalpay = (TextView) findViewById(R.id.tv_pay);
        remainderMoney = (TextView) findViewById(R.id.remainderMoney);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        initPayButton();


        pay_hint = (RelativeLayout) findViewById(R.id.pay_hint);

        orderList = (ListView) findViewById(R.id.orderList);
        orderAdapter = new OrderAdapter(this);
        orderList.setAdapter(orderAdapter);

        payOrderRequest = new PayOrderRequest();
        payOrderRequest.payOrderRequest(info, uid, this);//结算中心数据列表数据
        payRequest = new PayRequest();

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

    public void back(View v) {
        Intent intent = new Intent();
        intent.setAction(ConstantValues.REFRESH);
        mLocalBroadcastManager.sendBroadcast(intent);
        finish();
    }

    /*
     * 显示订单列表
     */
    public void showOrderList(View v) {
        if (isOrderShow) {
            orderList.setVisibility(View.VISIBLE);
        } else {
            orderList.setVisibility(View.GONE);
        }
        isOrderShow = !isOrderShow;
    }

    /**
     * 结算中心页面中的商品列表数据
     *
     * @param baseObjectBean
     */
    public void onPayOrderSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            payOrderBean = (PayOrderBean) baseObjectBean.getData();
            orderAdapter.addData(payOrderBean.getList());
            tv_totalpay.setText(getResources().getString(R.string.order_money, payOrderBean.getTotal_money()));//商品合计总价钱
            remainderMoney.setText(getResources().getString(R.string.remainder_money, payOrderBean.getUser_money()));//账户余额代替支付的钱数

            payData();

            netErrorView.loadSuccess();
            scrollView.setVisibility(View.VISIBLE);
        } else {
            netErrorView.loadFail();
        }
    }


    private void payData() {
        int payMoney = payOrderBean.getTotal_money();//商品合计总价钱
        String userMoney = payOrderBean.getUser_money();//账户余额代替支付的钱数
        Float selfMoney = Float.parseFloat(userMoney);
        if (selfMoney > 0) {
            checkbox.setChecked(true);
            checkbox.setVisibility(View.VISIBLE);
        } else {
            checkbox.setChecked(false);
            checkbox.setVisibility(View.GONE);
        }

        if (selfMoney >= payMoney) {
            checkbox.setText(getResources().getString(R.string.order_money, String.valueOf(payMoney)));
            payWay = false;//不用展示其他支付方式（微信或者网银）
            radioGroup.setVisibility(View.GONE);
            pay_hint.setVisibility(View.GONE);
        } else {
            checkbox.setText(getResources().getString(R.string.order_money, userMoney));
            money.setText(getResources().getString(R.string.pay_order_money_other, String.valueOf(payMoney - selfMoney)));//其他支付方式（微信或者网银）额外支付钱数
            payWay = true;
            radioGroup.setVisibility(View.VISIBLE);
            pay_hint.setVisibility(View.VISIBLE);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
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
        });


    }


    public void onPayOrderFailed(VolleyError error) {
        netErrorView.loadFail();
    }


    public void onReload() {
        payOrderRequest.payOrderRequest(info, uid, this);//结算中心数据列表数据
    }

    private float payMoneys;


    /**
     * 确认支付,按钮点击后触发
     * 支付流程:
     * 1.先充值,再余额支付
     * 2.如果余额大于需要支付的金额,直接使用余额支付
     *
     * @param v
     */
    public void pay(View v) {//// TODO: 2016/3/23
        if (payOrderBean == null) {
            return;
        }
        //总共需要花费金额
        int totalMoney = payOrderBean.getTotal_money();
        //账户余额
        float accountMoney = Float.parseFloat(payOrderBean.getUser_money());

        //额外需要支付金额（微信或者网银/其它）
        payMoneys =  payOrderBean.getPay_money();

        if (totalMoney > accountMoney) {
            OrderCodeRequest orderCodeRequest = new OrderCodeRequest();
            if (payType == PayType.WEIXIN) {
                orderCodeRequest.orderCodeRequest(uid, String.valueOf(payMoneys), "wxpay","shopping", this);//参数3为订单生成接口 回调方法：onOrderCodeSuccess
            } else if (payType == PayType.ALIPAY) {
                orderCodeRequest.orderCodeRequest(uid, String.valueOf(payMoneys), "alipay","shopping", this);//参数3为订单生成接口 回调方法：onOrderCodeSuccess
            } else if (payType == PayType.JDPAY) {
                orderCodeRequest.orderCodeRequest(uid, String.valueOf(payMoneys), "jdpay","shopping", this);//参数3为订单生成接口 回调方法：onOrderCodeSuccess
            } else if (payType == PayType.AIBEIPAY) {
                orderCodeRequest.orderCodeRequest(uid, String.valueOf(payMoneys), "iapppay","shopping", this);//参数3为订单生成接口 回调方法：onOrderCodeSuccess
            } else if (payType == PayType.HFBPAY) {
                orderCodeRequest.orderCodeRequest(uid, String.valueOf(payMoneys), "heepay","shopping", this);//参数3为订单生成接口 回调方法：onOrderCodeSuccess
            } else {
                ToastUtil.showToast(PayOrderActivity.this, "请选择支付方式");
            }
        } else {
            payBuyAccountMoney();
        }
    }

    //获取订单号,成功回调的方法
    public void onOrderCodeSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {//订单生成成功
            PayCodeBean payCodeBean = (PayCodeBean) baseObjectBean.getData();

            String order_id = payCodeBean.getOrder_code();//订单号

            if (payMoneys <= 0) {
                ToastUtil.showToast(PayOrderActivity.this, "网络较慢，稍后再试");
                return;
            }

            switch (payType) {
                case WEIXIN:
                    WeiXinPayUtils weiXinPayUtils = new WeiXinPayUtils(PayOrderActivity.this, String.valueOf(payMoneys), order_id);//拉起微信支付
                    break;
                case ALIPAY:
                    AlipayUtils alipayUtils = new AlipayUtils(PayOrderActivity.this);
                    alipayUtils.pay(String.valueOf(payMoneys), order_id);
                    break;
                case JDPAY:
                    startActivity(new Intent(PayOrderActivity.this, PayResultShowActivity.class).putExtra("MONEY", payMoneys).putExtra("ORDER_CODE", payCodeBean.getOrder_code()).putExtra("CALLBACK_URL", payCodeBean.getCallback_url()).putExtra("fromOrder", true));
                    PayOrderActivity.this.finish();
                    break;
                case AIBEIPAY:
                    IAppPayOrder.startPay(this, payMoneys, payCodeBean.getOrder_code(), payCodeBean.getCallback_url(), iPayResultCallback);
                    break;
                case HFBPAY:
                    request.get(HFBPAY, "cart/heepay?", "&pay_type=30" + "&pay_amt=" + payMoneys + "&code=" + payCodeBean.getOrder_code());
                    break;
            }

        } else {
//            ToastUtil.showToast(this, baseObjectBean.getMessage());// getMessage == 请选择正确的支付方式
            ToastUtil.showToast(this, "生成订单失败");
        }
    }

    /**
     * 支付结果回调
     */
    IPayResultCallback iPayResultCallback = new IPayResultCallback() {

        public void onPayResult(int resultCode, String signvalue, String resultInfo) {
            switch (resultCode) {
                case IAppPay.PAY_SUCCESS:
                    //调用 IAppPayOrderUtils 的验签方法进行支付结果验证(根据平台公钥验签)
                    boolean payState = IAppPayOrderUtils.checkPayResult(signvalue, PayConfig.publicKey);
                    if (payState) {
                        Toast.makeText(PayOrderActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                        payBuyAccountMoney();
                    }
                    break;
                case IAppPay.PAY_ING:
                    Toast.makeText(PayOrderActivity.this, "成功下单", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(PayOrderActivity.this, resultInfo + "----", Toast.LENGTH_LONG).show();//错误信息显示
                    break;
            }
        }
    };

    public void onOrderCodeFailed(VolleyError error) {

    }

    @Override
    public void OnPayByHCListenerSuccess(String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            int status = obj.getInt("status");
            if (status == 1) {
                String pay_callback_url = obj.getString("data");
                Intent intent = new Intent(PayOrderActivity.this, PayResultShowActivity.class).putExtra("PAY_CALLBACK_URL", pay_callback_url);
                startActivity(intent);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnPayByHCListenerFailed(VolleyError error) {

    }


    private void payBuyAccountMoney() {

        dialog = new ProgressDialog(this);
        //声明进度条对话框
        //创建ProgressDialog对象
        // 设置进度条风格，风格为圆形，旋转的
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("正在处理中...");
        dialog.setCanceledOnTouchOutside(false);
        // 设置ProgressDialog 标题图标
        //mDialog.setIcon(R.mipmap.ic_loading_wrapper_white);
        // 设置ProgressDialog 的进度条是否不明确
        dialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        dialog.setCancelable(false);
        // 让ProgressDialog显示
        dialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                List<PayOrderGoodsBean> payOrderGoodsBeans = payOrderBean.getList();
                String info = "";
                for (PayOrderGoodsBean payOrderGoodsBean : payOrderGoodsBeans) {
                    info = info + payOrderGoodsBean.getId() + "," + payOrderGoodsBean.getGonumber() + "|";
                }
                Log.e("TAG", "================" + info);
                payRequest.payRequest(info.substring(0, info.length() - 1), uid, PayOrderActivity.this);//参数3接口的回调方法onPaySuccess
            }
        }, 200);
    }

    /*
     * 现有账户支付成功处理（不是微信或者网银）
     */
    private List<WebViewGoolsListBean> list = new ArrayList<WebViewGoolsListBean>();

    public void onPaySuccess(BaseObjectBean baseObjectBean) {
        dialog.dismiss();
        if (baseObjectBean.getStatus() == 1) {

            //总共需要花费金额
            int totalMoney = payOrderBean.getTotal_money();
            //账户余额
            float accountMoney = Float.parseFloat(payOrderBean.getUser_money());
            if (totalMoney <= accountMoney) {
                float remainder = accountMoney - totalMoney;
                Pref_Utils.putString(this, "money", String.valueOf(remainder));
            } else {
                Pref_Utils.putString(this, "money", "0");
            }

            Intent clear = new Intent();
            clear.setAction("messageStateChange");
            clear.putExtra("count", 0);
            sendBroadcast(clear);

            Intent intent2 = new Intent();
            intent2.setAction(ConstantValues.REFRESH);
            mLocalBroadcastManager.sendBroadcast(intent2);

            WebViewCallBackBean webViewCallBackBean = (WebViewCallBackBean) baseObjectBean.getData();
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("title", getResources().getString(R.string.pay_result));
            // intent.putExtra("url", API.PAYSUCCESS + webViewCallBackBean.getCode());
            intent.putExtra("count", webViewCallBackBean.getCount());
            intent.putExtra("zongrenci", webViewCallBackBean.getZongrenci());

            list = webViewCallBackBean.getList();
            intent.putExtra("data", (Serializable) list);

            startActivity(intent);

            finish();
        } else {
            ToastUtil.showToast(this, baseObjectBean.getMessage()+"**");
        }
    }

    public void onPayFailed(VolleyError error) {
        dialog.dismiss();
        ToastUtil.showToast(this, "支付失败，请重试");
    }


    public boolean isAibeipay() {
        return aibeipay;
    }

    public void onResume() {
        super.onResume();
        TCAgent.onResume(this);
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        TCAgent.onPause(this);
        MobclickAgent.onPause(this);
    }

    enum PayType {
        NULL,WEIXIN, ALIPAY, JDPAY, AIBEIPAY, HFBPAY;
    }
}
