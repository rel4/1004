package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.OtherBeanLogin;
import com.hongbaogou.listener.OnOtherLoginListener;
import com.hongbaogou.utils.RequestManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by Administrator on 2015/12/5.
 */
public class OtherLoginRequest extends BaseRequest {

    private RequestQueue mQueue;

    private String urlRequest = "member/getInfo?";


    public void requestOtherLogin(final OnOtherLoginListener onOtherLoginListener, final String openid, final String type, final String name, final String head_icon_url) {

        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();

        String encodeName = null;
        try {
            encodeName = URLEncoder.encode(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String url = urlBase + urlRequest + getParams() + "&openid=" + openid + "&type=" + type+ "&name=" + encodeName+ "&img=" + head_icon_url;

        System.out.println("============第三方登陆url===="+url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);
                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<OtherBeanLogin>>() {
                });
                System.out.println("============第三方登陆onResponse====" + s);
                //成功
                onOtherLoginListener.requestOnOtherLoginSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //失败
                onOtherLoginListener.requestOnOtherLoginFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
