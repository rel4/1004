package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.JoinPeopleObject;
import com.hongbaogou.listener.OnJoinPeopleListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/11/29.
 */
public class JoinPeopleRequest extends BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "goods/get_record_list?";

    public void goodsListRequest(String shopid, String issue, int pageNo, final OnJoinPeopleListener onJoinPeopleListener) {
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams()+"&shopid=" + shopid + "&qishu=" + issue + "&size=10&page=" + pageNo;
        Log.e("url", url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<JoinPeopleObject>>() {});
                        onJoinPeopleListener.onJoinPeopleSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onJoinPeopleListener.onJoinPeopleFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
