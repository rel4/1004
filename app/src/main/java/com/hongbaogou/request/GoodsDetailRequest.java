package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.GoodsDetailBean;
import com.hongbaogou.listener.OnGoodsDetailListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/11/30.
 */
public class GoodsDetailRequest extends BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "goods/get_goods_info?";


    public void goodsDetailRequest(String id ,String uid,final OnGoodsDetailListener onGoodsDetailListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams()+ "&id="+id+"&uid="+uid;
        System.out.println("-------商品详情url--------"+url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        System.out.println("-------商品详情response--------"+response);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<GoodsDetailBean>>() {});
                        onGoodsDetailListener.onGoodsDetailSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onGoodsDetailListener.onGoodsDetailFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }
}
