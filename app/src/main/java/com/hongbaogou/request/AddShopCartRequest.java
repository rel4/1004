package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.YYJXApplication;
import com.hongbaogou.bean.MessageCountBean;
import com.hongbaogou.listener.OnAddShopCartListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/4.
 */
public class AddShopCartRequest extends BaseRequest{


    private RequestQueue mQueue;
    private String urlRequest = "cart/do_add_cart?";


    public void AddShopCartRequest(String uid ,String num,String id ,final OnAddShopCartListener onAddShopCartListener){

        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams()+"&id="+id+"&num="+num+"&cache_id="+ YYJXApplication.DEVICE_ID + "&uid="+ uid;
        Log.d("url", url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        MessageCountBean messageCountBean = JSON.parseObject(substring, MessageCountBean.class);
                        onAddShopCartListener.onAddShopCartSuccess(messageCountBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onAddShopCartListener.onAddShopCartFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    };

}
