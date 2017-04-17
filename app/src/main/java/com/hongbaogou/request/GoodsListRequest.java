package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.GoodsObjectBean;
import com.hongbaogou.listener.OnGoodsListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/11/27.
 */
public class GoodsListRequest extends BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "index/get_type_goods_list?";

    public void goodsListRequest(String type, int pageNo, final OnGoodsListener onGoodsListener) {
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest +"type=" + type + "&page=" + pageNo +"&"+ getParams();
        System.out.println("商品首页-------"+url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {

                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );

                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<GoodsObjectBean>>() {});
                        onGoodsListener.onGoodsSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onGoodsListener.onGoodsFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
