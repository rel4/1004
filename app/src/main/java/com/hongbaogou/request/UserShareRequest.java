package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.BeanUserShare;
import com.hongbaogou.listener.OnUserShareListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2016/1/3.
 */
public class UserShareRequest extends BaseRequest {
    private RequestQueue mQueue;

    private String urlUserQuest = "member/get_member_shaidan?";

    public void requestUserShare(final OnUserShareListener onUserShareListener, int page, String uid) {
        String url = urlBase + urlUserQuest + getParams() + "&uid=" + uid + "&size=" + 6 + "&page=" + page;
        Log.e("TAG", "----->>>url = " + url);
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String ss) {
                String substring = ss.substring(ss.indexOf("{"), ss.lastIndexOf("}") + 1);
                Log.d("substring", substring);
                BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<BeanUserShare>>() {
                });
                onUserShareListener.requestUserShareSuccess(baseListBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
                onUserShareListener.requestUserShareFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
