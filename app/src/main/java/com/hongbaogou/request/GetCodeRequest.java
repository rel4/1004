package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.EmptyBean;
import com.hongbaogou.listener.OnGerCodeListener;
import com.hongbaogou.utils.RequestManager;


/**
 * Created by Administrator on 2015/12/5.
 * <p/>
 * 获取验证码的请求
 */
public class GetCodeRequest extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/do_send_mobile_code?";

    private String urlFindRequest = "member/do_send_found_code?";

    /**
     * 获取注册的验证码
     *
     * @param onGerCodeListener
     * @param mobile
     */
    public void requestGetCode(final OnGerCodeListener onGerCodeListener, String mobile) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();

        System.out.println("-------验证码url------"+urlBase + urlRequest + getParams() + "&mobile=" + mobile);

        StringRequest stringRequest = new StringRequest(urlBase + urlRequest + getParams() + "&mobile=" + mobile, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);


                BaseObjectBean baseObjectBean =
                        JSON.parseObject(substring, new TypeReference<BaseObjectBean<EmptyBean>>() {
                        });
                System.out.println("-------验证码response------" +s);
                onGerCodeListener.requestGerCodeSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onGerCodeListener.requestGerCodeFailed(volleyError);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 1, 1.0f));

        RequestManager.addRequest(stringRequest, mQueue);
    }

    /**
     * 获取找回的验证码
     *
     * @param onGerCodeListener
     * @param mobile
     */
    public void requestGetFindCode(final OnGerCodeListener onGerCodeListener, String mobile) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();

        System.out.println("-------找回密码url------"+urlBase + urlFindRequest + getParams() + "&mobile=" + mobile);

        StringRequest stringRequest = new StringRequest(urlBase + urlFindRequest + getParams() + "&mobile=" + mobile, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);
                BaseObjectBean baseObjectBean =
                        JSON.parseObject(substring, new TypeReference<BaseObjectBean<EmptyBean>>() {
                        });
                System.out.println("-------找回密码response------" + s);
                onGerCodeListener.requestGerCodeSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onGerCodeListener.requestGerCodeFailed(volleyError);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 1, 1.0f));

        RequestManager.addRequest(stringRequest, mQueue);
    }
}
