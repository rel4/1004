package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.RechargeBean;
import com.hongbaogou.listener.OnRechargeListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class RechargeRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "cart/do_add_money_code?";

    public void rechargeRequests(String uid,String money,String paytype,String payway,final OnRechargeListener onRechargeListener){
        mQueue = RequestManager.getRequestQueue();

        String url=urlBase+urlRequest+getParams()+"&uid="+uid+"&money="+money+"&pay_type="+paytype + "&pay_way="+payway;

        System.out.println("------充值生成订单url---------"+url);

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        System.out.println("------充值生成订单response---------"+response);

                        BaseObjectBean baseObjectBean = JSON.parseObject(substring,new TypeReference<BaseObjectBean<RechargeBean>>(){});
                        onRechargeListener.OnRechargeListenerSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGW", error.getMessage(), error);
                onRechargeListener.OnRechargeListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }
}
