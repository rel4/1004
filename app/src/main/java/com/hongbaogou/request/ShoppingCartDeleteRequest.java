package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.YYJXApplication;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.EmptyBean;
import com.hongbaogou.listener.OnShoppingCartDeleteListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ShoppingCartDeleteRequest extends BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "cart/do_del_cart?";


    public void ShoppingCartDeleteRequest(String uid, String ids, final OnShoppingCartDeleteListener onShoppingCartDeleteListener) {
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams()+"&id="+ids+"&uid="+ uid+"&cache_id="+ YYJXApplication.DEVICE_ID;
        Log.e("url", url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<EmptyBean>>(){});
                        onShoppingCartDeleteListener.onShoppingCartDeleteSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onShoppingCartDeleteListener.onShoppingCartDeleteFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }

}
