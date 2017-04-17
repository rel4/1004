package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.WinInfoBean;
import com.hongbaogou.listener.OnWinInfoListener;
import com.hongbaogou.utils.RequestManager;

/**
 * 获取中奖者信息
 */
public class WinInfoRequest extends  BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "goods/get_win_member_info?";
    private int positon;
    public WinInfoRequest(int positon){
        this.positon = positon;
    }

    public void WinInfoRequest(String id ,final OnWinInfoListener onWinInfoListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams() + "&id=" + id;
        Log.e("url", url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<WinInfoBean>>() {});
                        onWinInfoListener.onWinInfoSuccess(baseObjectBean,positon);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onWinInfoListener.onWinInfoFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    };

}
