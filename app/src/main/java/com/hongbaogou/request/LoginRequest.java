package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.YYJXApplication;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanLogin;
import com.hongbaogou.listener.OnAllLogListener;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.RequestManager;


/**
 * Created by Administrator on 2015/12/5.
 */
public class LoginRequest extends BaseRequest {

    private RequestQueue mQueue;

    private String urlRequest = "member/do_login?";


    public void requestAllLog(final OnAllLogListener onAllLogListener, final String username, final String password) {


        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();

        final String url = urlBase + urlRequest + getParams() + "&username=" + username + "&password=" + password;

        System.out.println("--------账号登陆url--------"+url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);

                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<BeanLogin>>() {
                });
                BaseObjectBean data = baseObjectBean.getData();
                if (data instanceof  BeanLogin){
                    BeanLogin beanLogin = (BeanLogin) data;
                    Pref_Utils.putString(YYJXApplication.applicationContext, "USER_AUTHKEY",beanLogin.getAuthkey());
                }

                System.out.println("--------账号登陆response--------" + s);
                //成功
                onAllLogListener.requestAllLogSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //失败
                onAllLogListener.requestAllLogFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
