package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanShareDetails;
import com.hongbaogou.listener.OnShareDetailsListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ShareDetailsRequest extends BaseRequest {
    private RequestQueue mQueue;
    private String urlRequest = "goods/get_shaidan_info?";

    public void requestShareDetails(final OnShareDetailsListener onShareDetailsListener, String id) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();
        StringRequest stringRequest = new StringRequest(urlBase + urlRequest + getParams() + "&sd_id=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);
                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<BeanShareDetails>>() {
                });
                Log.e("TAG", s);
                onShareDetailsListener.requestShareDetailsSuccess(baseObjectBean);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
                onShareDetailsListener.requestShareDetailsFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
