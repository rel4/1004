package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.BeanFindData;
import com.hongbaogou.listener.OnFindDataListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class FindDataRequest extends BaseRequest {

    private int TYPE = 1;
    private RequestQueue mQueue;
    private String urlRequest = "index/get_article_list?";

    public void requestFindData(final OnFindDataListener onFindDataListener) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams() + "&type=" + TYPE;
        Log.e("TAG", "---> url = " + url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //成功请求的回调
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);
                BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<BeanFindData>>() {
                });
                onFindDataListener.requestFindDataSuccess(baseListBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //失败请求的回调
                Log.e("TAG", volleyError.getMessage(), volleyError);
                onFindDataListener.requestFindDataFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
