package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.YYJXApplication;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.ShoppingCartBean;
import com.hongbaogou.listener.OnShoppingCartListListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ShoppingCartListRequest extends  BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "cart/do_show_cart?";


    public void shoppingCartListRequest(String uid ,final OnShoppingCartListListener onShoppingCartListListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams()+"&cache_id="+ YYJXApplication.DEVICE_ID + "&uid=" + uid;
        System.out.println("------购物车清单url--------" + url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {

                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);

                        System.out.println("------购物车清单列表--------"+response);
                        BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<ShoppingCartBean>>() {});
                        onShoppingCartListListener.onShoppingCartListSuccess(baseListBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onShoppingCartListListener.onShoppingCartListFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    };
}
