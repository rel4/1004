package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.EmptyBean;
import com.hongbaogou.listener.OnRegisterOkListener;
import com.hongbaogou.utils.RequestManager;


/**
 * Created by Administrator on 2015/12/5.
 * <p/>
 * 短信注册获取验证码后,保存登录
 */
public class RegisterOkRequest extends BaseRequest {

    private RequestQueue mQueue;
    //请求密码的
    private String urlRequest = "member/do_register?";

    private String findUrlRequest = "member/do_save_new_password?";

    public void requestRegisterOk(final OnRegisterOkListener onRegisterOkListener, String acc, String pswd, String code) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams() + "&mobile=" + acc + "&code=" + code + "&password=" + pswd;
        Log.e("TAG", " ====== " + url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring",substring );
                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<EmptyBean>>() {
                });
                //成功
                onRegisterOkListener.requestRegisterOKSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //失败
                onRegisterOkListener.requestRegisterOKFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }


    public void requestFindOutOk(final OnRegisterOkListener onRegisterOkListener, String acc, String pswd, String code) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();
        String s = "&mobile=15361550617&password=123456&code=123456";
        String url = urlBase + findUrlRequest + getParams() + "&mobile=" + acc + "&code=" + code + "&password=" + pswd;
        Log.e("TAG", "url =  " + url+"----------");
        StringRequest stringRequest = new StringRequest(url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);

                Log.e("TAG", "----requestFindOutOk---->>" + substring);
                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<EmptyBean>>() {
                });
                //成功
                onRegisterOkListener.requestRegisterOKSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //失败
                onRegisterOkListener.requestRegisterOKFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
