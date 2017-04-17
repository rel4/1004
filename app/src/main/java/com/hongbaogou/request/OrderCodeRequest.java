package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.PayCodeBean;
import com.hongbaogou.listener.OnOrderCodeListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/29.
 */
public class OrderCodeRequest extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "cart/do_add_money_code?";

    public void orderCodeRequest(String uid, String money,String paytype, String payway,final OnOrderCodeListener onOrderCodeListener) {
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams() + "&uid=" + uid + "&money=" + money + "&pay_type="+paytype + "&pay_way="+payway;
        System.out.println("------支付生成订单url------" + url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        System.out.println("--------支付生成订单response--->" + response);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<PayCodeBean>>() {
                        });

                        onOrderCodeListener.onOrderCodeSuccess(baseObjectBean);
                    }
                },

                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        onOrderCodeListener.onOrderCodeFailed(error);
                    }
                });
        RequestManager.addRequest(stringRequest, mQueue);
    }

    ;
}
