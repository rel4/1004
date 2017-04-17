package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.WinCodeBean;
import com.hongbaogou.listener.OnQrCodeListener;
import com.hongbaogou.utils.RequestManager;

/**
 * 获取中奖者信息
 */
public class QrCodeRequest extends  BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "member/get_setting_info?";

    public void WinInfoRequest(String user_id,final OnQrCodeListener onQrCodeListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams()+"&uid="+user_id;


        System.out.println("--------生成二维码url--------" + url);

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        System.out.println("--------生成二维码response--------" + response);

                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<WinCodeBean>>() {});
                        onQrCodeListener.OnQrCodeListenerSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onQrCodeListener.OnQrCodeListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    };

}
