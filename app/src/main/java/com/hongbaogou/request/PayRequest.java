package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.YYJXApplication;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.WebViewCallBackBean;
import com.hongbaogou.listener.OnPayListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/9.
 */
public class PayRequest extends  BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "cart/do_pay?";

    public void payRequest(String info ,String uid ,final OnPayListener onPayListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams() + "&uid=" + uid + "&info=" + info +"&cache_id=" + YYJXApplication.DEVICE_ID;
        System.out.println("-----实际支付url-----"+url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        System.out.println("-----实际支付-----" + response);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<WebViewCallBackBean>>() {});
                        onPayListener.onPaySuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                System.out.println("-----实际支付Error-----" + error.getMessage()+"------"+error);
                onPayListener.onPayFailed(error);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 1, 1.0f));

        RequestManager.addRequest(stringRequest,mQueue);
    };
}
