package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.WinAgoObjectBean;
import com.hongbaogou.listener.OnWinAgoListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/8.
 */
public class WinAgoRequest extends BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "goods/get_old_lottery?";

    public void winInfoRequest(int pageNo,String sid ,final OnWinAgoListener onWinAgoListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams() + "&sid=" + sid + "&size=8&page="+pageNo;
        System.out.println("-----往期揭晓url-------"+url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        System.out.println("-----往期揭晓response-------" + response);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<WinAgoObjectBean>>(){});
                        onWinAgoListener.onWinAgoSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onWinAgoListener.onWinAgoFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    };
}
