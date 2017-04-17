package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanAndroid;
import com.hongbaogou.listener.OnGetVersionListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class GetVersionRequest extends BaseRequest {
    private RequestQueue mQueue;
    private String urlRequest = "member/do_check_update?";

    public void requestGetVersion(final OnGetVersionListener onGetVersionListener) {
        //获取请求队列对象
        mQueue = RequestManager.getRequestQueue();
        StringRequest stringRequest = new StringRequest(urlBase + urlRequest + getParams(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                Log.d("substring",substring );
                BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<BeanAndroid>>() {
                });
                onGetVersionListener.requestGetVersionSuccess(baseObjectBean);
                Log.e("TAG", "----GetVersion-----" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", "-----GetVersion-----" + volleyError.getMessage(), volleyError);
                onGetVersionListener.requestGetVersionFailed(volleyError);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
