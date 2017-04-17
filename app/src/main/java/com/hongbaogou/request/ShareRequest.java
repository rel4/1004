package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.ShareObjectBean;
import com.hongbaogou.listener.OnShareListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class ShareRequest extends BaseRequest {
    private RequestQueue mQueue;
    private String urlRequest = "goods/get_shaidan_list?";

    public void requestShare(final OnShareListener onShareListener, int page, String sid) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();

        System.out.println("--------晒单分享url--------"+urlBase + urlRequest + getParams() + "&sid=" + sid + "&size=" + 6 + "&page=" + page);

        StringRequest stringRequest = new StringRequest(urlBase + urlRequest + getParams() + "&sid=" + sid + "&size=" + 6 + "&page=" + page, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);
                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<ShareObjectBean>>() {
                });
                System.out.println("--------晒单分享response--------"+s);
                onShareListener.requestShareSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
                onShareListener.requestShareFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
