package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BannerBean;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.listener.OnBannerListener;
import com.hongbaogou.utils.RequestManager;

/**
 * 主页顶部滑动图片的请求
 */
public class LoadingBannerRequest extends BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "index/get_slide_list?";

    public void bannerRequest(final OnBannerListener onBannerListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams();
        Log.e("url", url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {

                        System.out.println("--------response------"+response);

                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);

                        BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<BannerBean>>(){});
                        onBannerListener.onBannerSuccess(baseListBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onBannerListener.onBannerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    };

}
