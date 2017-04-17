package com.hongbaogou.request;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by intasect on 2016/11/21.
 */
public class BackToWebRequest extends BaseRequest {
    private RequestQueue mQueue;
    private String urlRequest = "member/do_openapp?";

    public void backToWebRequest(String uid,String type){
        mQueue = RequestManager.getRequestQueue();
        String   url = urlBase + urlRequest + getParams() + "&uid=" + uid+"&type="+type;
        System.out.println("-------返回数据到后台url----------"+url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        System.out.println("-------返回数据到后台success----------");
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
