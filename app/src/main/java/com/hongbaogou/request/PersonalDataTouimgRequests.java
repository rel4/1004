package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.HeadImageBean;
import com.hongbaogou.listener.OnPersonalDataTouimgListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class PersonalDataTouimgRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/do_upload_touimg";
    public void personalDataTouimgRequests(final OnPersonalDataTouimgListener onPersonalDataTouimgListener){
        mQueue = RequestManager.getRequestQueue();
        String url =  urlBase+urlRequest;
       // url =   "http://192.168.1.50/index.php/yungouapi/member/do_upload_touimg";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        HeadImageBean headImageBean = JSON.parseObject(substring, HeadImageBean.class);
                        onPersonalDataTouimgListener.OnPersonalDataTouimgListenerSuccess(headImageBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGS", error.getMessage(), error);
                onPersonalDataTouimgListener.OnPersonalDataTouimgListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }
}
