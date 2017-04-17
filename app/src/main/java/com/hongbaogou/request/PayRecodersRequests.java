package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.PayRecodersBean;
import com.hongbaogou.listener.OnPayRecodersListListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class PayRecodersRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/get_member_recharge_records?";

    public void payRecodersRequest(String uid, int pageNo, final OnPayRecodersListListener onPayRecodersListListener) {
        mQueue = RequestManager.getRequestQueue();
        String url = null;
        if ("-1".equalsIgnoreCase(uid)) {
            url = urlBase + urlRequest + getParams() + "&page=" + pageNo;
        } else {
            url = urlBase + urlRequest + getParams() + "&uid=" + uid + "&size=10&page=" + pageNo;

        }

        System.out.println("-------充值记录url----------"+url);

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        System.out.println("-------充值记录response----------" + response);
                        BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<PayRecodersBean>>() {
                        });

                        onPayRecodersListListener.OnPayRecodersListListenerSuccess(baseListBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onPayRecodersListListener.OnPayRecodersListListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
