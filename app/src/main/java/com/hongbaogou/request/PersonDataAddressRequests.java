package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.PersonAddressBean;
import com.hongbaogou.listener.OnPersonDataAddressListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class PersonDataAddressRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/get_member_address?";
    public void personDataAddressRequests(String uid ,final OnPersonDataAddressListener onPersonDataAddressListener){
        mQueue = RequestManager.getRequestQueue();
        String url =  urlBase+urlRequest+getParams()+"&uid="+uid;

        System.out.println("------地址列表url-----"+url);

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        System.out.println("------地址列表-----"+response);
                       BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<PersonAddressBean>>() {});

                        onPersonDataAddressListener.OnPersonDataAddressListenerSuccess(baseListBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onPersonDataAddressListener.OnPersonDataAddressListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }
}
