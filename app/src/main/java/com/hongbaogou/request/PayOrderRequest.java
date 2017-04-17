package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.YYJXApplication;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.PayOrderBean;
import com.hongbaogou.listener.OnPayOrderListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/9.
 */
public class PayOrderRequest extends BaseRequest{
    private RequestQueue mQueue;
    private String urlRequest = "cart/get_pay_info?";

    public void payOrderRequest(String info ,String uid ,final OnPayOrderListener onPayOrderListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams() + "&uid=" + uid + "&info=" + info + "&cache_id=" + YYJXApplication.DEVICE_ID;
        System.out.println("--------结算中心商品信息url-------"+url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        System.out.println("---------结算中心商品信息response-------" + response);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<PayOrderBean>>(){});
                        onPayOrderListener.onPayOrderSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onPayOrderListener.onPayOrderFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    };




}
