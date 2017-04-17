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
import com.hongbaogou.listener.OnPersonalDataSaveAddressListener;
import com.hongbaogou.utils.RequestManager;

public class PersonalDataSaveAddressRequests extends BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "member/do_save_member_address?";
    public void personalDataSaveAddressRequests(String uid,String s,String is_default,String id, final OnPersonalDataSaveAddressListener onPersonalDataSaveAddressListener){
        mQueue = RequestManager.getRequestQueue();
        String url =  urlBase+urlRequest+getParams()+"&uid="+uid+s+"&default="+is_default+"&id"+id;

        System.out.println("--------添加地址url--------"+url);

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        System.out.println("--------添加地址--------"+response);
                       BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<EmptyBean>>() {});
                        onPersonalDataSaveAddressListener.OnPersonalDataSaveAddressListenerSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGS", error.getMessage(), error);
                onPersonalDataSaveAddressListener.OnPersonalDataSaveAddressListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }


}
