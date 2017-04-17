package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanAllLog;
import com.hongbaogou.listener.OnAllLogListener;
import com.hongbaogou.utils.RequestManager;


/**
 * Created by Administrator on 2015/12/5.
 */
public class AllLogRequest extends BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "member/get_duobao_list?";


    public void requestAllLog(final OnAllLogListener onAllLogListener, String uid, int page, int type) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams() + "&uid=" + uid + "&size=8&page=" + page + "&type=" + type;
        System.out.println("----夺宝记录url-----"+url);
        StringRequest stringRequest = new StringRequest(url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                Log.d("substring", substring);
                System.out.println("----夺宝记录-----" + response);
                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<BeanAllLog>>(){});
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
