package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.CategoryListBean;
import com.hongbaogou.listener.OnCategoryListener;
import com.hongbaogou.utils.RequestManager;

/**
 * 主页栏目的请求
 */
public class CategoryRequest extends  BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "index/get_four_icon?";


    public void categoryRequest(final OnCategoryListener onCategoryListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams();;

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {

                        System.out.println("--------response------"+response);

                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<CategoryListBean>>() {});
                        onCategoryListener.onCategorySuccess(baseListBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onCategoryListener.onCategoryFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    };
}
