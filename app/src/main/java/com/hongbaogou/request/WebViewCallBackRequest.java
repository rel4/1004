package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.WebViewCallBackBean;
import com.hongbaogou.listener.OnWebViewListener;
import com.hongbaogou.utils.RequestManager;

/**
 * 获取c
 */
public class WebViewCallBackRequest extends  BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "cart/do_get_shoplist?";
    public void webViewCallBackRequest(String uid ,final OnWebViewListener onWebViewListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams() + "&uid=" + uid;
        Log.d("url", url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<WebViewCallBackBean>>() {});
                        onWebViewListener.OnWebViewListenerSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onWebViewListener.OnWebViewListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    };

}
