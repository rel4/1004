package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanMyShareObject;
import com.hongbaogou.listener.OnMyShareListener;
import com.hongbaogou.utils.RequestManager;


/**
 * Created by Administrator on 2015/12/5.
 */
public class MyShareRequest extends BaseRequest {

    private RequestQueue mQueue;

    //煤业请求6条数据
    private int NUMBER = 6;

    private String urlRequest = "member/get_my_shaidan?";


    public void requestMyShare(final OnMyShareListener onMyShareListener, String uid, int page) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();

        System.out.println("-----我的晒单url-------"+urlBase + urlRequest + getParams() + "&uid=" + uid + "&size=" + NUMBER + ".&page=" + page);

        StringRequest stringRequest = new StringRequest(urlBase + urlRequest + getParams() + "&uid=" + uid + "&size=" + NUMBER + ".&page=" + page, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println("-----我的晒单response-------" + s);
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring", substring);
                BaseObjectBean baseObjectBean =
                        JSON.parseObject(substring, new TypeReference<BaseObjectBean<BeanMyShareObject>>() {
                        });
                onMyShareListener.requestMyShareSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onMyShareListener.requestMyShareFailed(volleyError);
                Log.d("TAG", "------MyShare------" + volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
