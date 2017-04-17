package com.hongbaogou.pay.constants;


/**
 * 微信支付,配置参数
 */
public class Constants {

    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wx9c103ade3dd2ac7f"/>为新设置的appid
    public static final String APP_ID = "wx9c103ade3dd2ac7f";

    //商户号
    public static final String MCH_ID = "1402649102";

    //  API密钥，在商户平台设置
    public static final String API_KEY = "I4BeGkHp6OP8yElfrirOUYGSIpyHudl1";

    //微信回调notify_url
    public static final String NOTIFY_URL = "http://v2.qcread.com/index.php/pay/wxpay_url/houtai2";
}
