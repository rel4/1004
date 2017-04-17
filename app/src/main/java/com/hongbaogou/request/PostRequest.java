package com.hongbaogou.request;

import android.util.Log;

import com.hongbaogou.utils.MD5;

/**
 * Created by Administrator on 2015/12/15.
 */
//post上传头像
public class PostRequest {

    protected String urlBase = "http://192.168.1.50/index.php/yungouapi/member/do_upload_touimg";
    private String partner = "ygandroid";
    private String key = "yungou888";

    protected String getParams() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        Log.d("timestamp", timestamp);
        String sign = MD5.getStringMD5(partner + timestamp + key);
        Log.d("sign", sign);
        return "partner=" + partner + "&timestamp=" + timestamp + "&sign=" + sign;
    }
}
