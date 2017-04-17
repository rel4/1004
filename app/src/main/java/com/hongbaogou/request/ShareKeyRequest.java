package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BeanKey;
import com.hongbaogou.listener.OnShareKeyListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/9.
 */
public class ShareKeyRequest extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "index/get_search_hot_words?";

    public void requestKey(final OnShareKeyListener onShareKeyListener) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();

        StringRequest stringRequest = new StringRequest(urlBase + urlRequest + getParams(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);
                BeanKey beanKey = JSON.parseObject(substring, new TypeReference<BeanKey>() {
                });
                onShareKeyListener.requestShareKeySuccess(beanKey);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onShareKeyListener.requestShareKeyFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
