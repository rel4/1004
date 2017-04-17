package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.GoodsObjectCategoryBean;
import com.hongbaogou.listener.OnGoodsCategoryListListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class GoodsCategoryRequest extends BaseRequest {

    private RequestQueue mQueue;

    public void goodsCategoryRequest(String cateid, int pageNo, final OnGoodsCategoryListListener onGoodsCategoryListListener) {
        mQueue = RequestManager.getRequestQueue();
        String url = null;
        if ("-1".equalsIgnoreCase(cateid)) {
            url = urlBase + "index/get_ten_yuan_list?" + getParams() + "&size=8&page=" + pageNo;
        } else {
            url = urlBase + "index/get_cate_goods_list?" + getParams() + "&cateid=" + cateid + "&size=8&page=" + pageNo;
        }
        Log.d("url", "===========================" + url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<GoodsObjectCategoryBean>>() {
                        });
                        onGoodsCategoryListListener.onGoodsCategoryListSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onGoodsCategoryListListener.onGoodsCategoryListFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
