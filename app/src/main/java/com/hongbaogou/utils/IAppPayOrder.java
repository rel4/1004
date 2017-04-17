package com.hongbaogou.utils;

import android.app.Activity;

import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iapppay.sdk.main.IAppPayOrderUtils;

/**
 * Created by Administrator on 2015/12/29.
 */
public class IAppPayOrder {


    public static void startPay(Activity activity,float price,String orderId,String callBackUrl,IPayResultCallback iPayResultCallback) {
        IAppPayOrderUtils orderUtils = new IAppPayOrderUtils();
        //应用id
        orderUtils.setAppid(PayConfig.appid);
        orderUtils.setWaresid(1);
        orderUtils.setCporderid(orderId);
        orderUtils.setAppuserid(PayConfig.userid);
        orderUtils.setPrice(price);//单位 元
        orderUtils.setWaresname("360夺宝");//开放价格名称(用户可自定义，如果不传以后台配置为准)
        orderUtils.setCpprivateinfo("android");
        orderUtils.setNotifyurl(callBackUrl);
        String param = orderUtils.getTransdata(PayConfig.privateKey);

        IAppPay.startPay(activity, param, iPayResultCallback);
    }


}
