package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.BeanClassfy;
import com.hongbaogou.listener.OnClassfyListener;
import com.hongbaogou.utils.RequestManager;


/**
 * Created by Administrator on 2015/11/27.
 */
public class ClasscyDataRequest extends BaseRequest {

    private RequestQueue mQueue;

    private String urlRequest = "index/get_category_list?";

    public void dataRequest(final OnClassfyListener onClassfyListener) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();

        /**
         * 请求失败结果回调
         */
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
                onClassfyListener.requestClassfyDataFailed(volleyError);
            }
        };

        StringRequest stringRequest = new StringRequest(urlBase + urlRequest + getParams(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring",substring );
                BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<BeanClassfy>>() {
                });
                onClassfyListener.requestClassfyDataSuccess(baseListBean);
            }
        }, error);
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
