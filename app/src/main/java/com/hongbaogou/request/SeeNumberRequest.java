package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanSeeNumber;
import com.hongbaogou.listener.OnAllNumberDataListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class SeeNumberRequest extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "goods/show_duobao_number?";

    public void requestSeeNumber(final OnAllNumberDataListener onAllNumberDataListener, String id, String uid, String qishu) {

        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();

        String url = urlBase + urlRequest + getParams() + "&id=" + id + "&uid=" + uid + "&qishu=" + qishu;
        Log.e("TAG", "夺宝号码详情--->id = " + id + "--->qishu = " + qishu + "--->uid = " + uid + "\nurl = " + url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);
                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<BeanSeeNumber>>() {
                });
                onAllNumberDataListener.requestAllNumberDataSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
                onAllNumberDataListener.requestAllNumberDataFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }

    public void requestSeeNumber(final OnAllNumberDataListener onAllNumberDataListener, String rid) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();

        String url = urlBase + urlRequest + getParams() + "&rid=" + rid;
        Log.e("TAG", "夺宝号码详情 --->rid = " + rid + "\nurl = " + url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring",substring );

                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<BeanSeeNumber>>() {
                });
                onAllNumberDataListener.requestAllNumberDataSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
                onAllNumberDataListener.requestAllNumberDataFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
