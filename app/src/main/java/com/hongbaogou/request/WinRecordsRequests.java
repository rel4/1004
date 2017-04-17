package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.WinRecodersBean;
import com.hongbaogou.listener.OnWinRecodersListListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class WinRecordsRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/get_member_win_records?";

    public void payRecodersRequest(String uid, int pageNo, final OnWinRecodersListListener onWinRecodersListListener) {
        mQueue = RequestManager.getRequestQueue();
        String url = null;
        if ("-1".equalsIgnoreCase(uid)) {
            url = urlBase + urlRequest + getParams() + "&page=" + pageNo;
        } else {
            url = urlBase + urlRequest + getParams() + "&uid=" + uid + "&size=8&page=" + pageNo;
        }

        System.out.println("------中奖记录url------"+url);

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        System.out.println("------中奖记录------" + response);
                        BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<WinRecodersBean>>() {
                        });
                        onWinRecodersListListener.OnWinRecodersListListenerSuccess(baseListBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onWinRecodersListListener.OnWinRecodersListListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
