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
import com.hongbaogou.listener.OnIssueShareListener;
import com.hongbaogou.utils.RequestManager;


/**
 * Created by Administrator on 2015/12/5.
 */
public class IssueShareRequest extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/do_shaidan";

    //private String url = "http://192.168.1.50/index.php/yungouapi/member/do_shaidan";

    public void requestIssueShare(final OnIssueShareListener onIssueShareListener) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();
        StringRequest stringRequest = new StringRequest(urlBase + urlRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("TAG", "----IssueShare-----" + s);
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring",substring );
                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<EmptyBean>>() {
                });
                Log.e("TAG", "-----IssueShare------" + s);
                //成功
                onIssueShareListener.requestIssueShareSuccess(baseObjectBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //失败
                onIssueShareListener.requestIssueShareFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
