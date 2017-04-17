package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanMoney;
import com.hongbaogou.listener.OnMoneyFreshListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by intasect on 2016/11/21.
 */
public class MoneyRequest extends BaseRequest {
    private RequestQueue mQueue;
    private String urlRequest = "member/get_user_info?";

    public void moneyRequest(final OnMoneyFreshListener onMoneyFreshListener, String uid){
        mQueue = RequestManager.getRequestQueue();
        String   url = urlBase + urlRequest + getParams() + "&uid=" + uid;
        System.out.println("-------获取金额url----------"+url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);

                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<BeanMoney>>() {
                        });
                        onMoneyFreshListener.requestMoneyFreshSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                //失败
                onMoneyFreshListener.requestMoneyFreshFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
