package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.GoodsObjectCategoryBean;
import com.hongbaogou.listener.OnSearchResultListener;
import com.hongbaogou.utils.RequestManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2015/12/3.
 */
public class SearchRequest extends BaseRequest {
    private RequestQueue mQueue;

    private String urlRequest = "goods/do_search?";

    public void searchResult(final OnSearchResultListener onSearchResultListener, String key) {

        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();
        String str = "";
        try {
            str = URLEncoder.encode(key, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(urlBase + urlRequest + getParams() + "&keywords=" + str, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring",substring );
                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<GoodsObjectCategoryBean>>() {
                });
                onSearchResultListener.requestSearchResultSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
                onSearchResultListener.requestSearchResultFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
