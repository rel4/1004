package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.BeanBestNewObject;
import com.hongbaogou.listener.OnBestNewListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/11/28.
 */
public class BestNewRequest extends BaseRequest{



    private String urlRequest = "goods/get_newest_lottery?";

    private RequestQueue mQueue;

    public void requestBestNewData(int size, int page,final OnBestNewListener onBestNewListener) {
        //获取queue对象
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams()+ "&size="+size+"&page="+page;

        System.out.println("------最新揭晓url------"+url);

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        //成功请求执行这个方法
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        System.out.println("------最新揭晓response------" + response);
                        BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<BeanBestNewObject>>() {});
                        //成功的接口回调
                        onBestNewListener.requestBestNewDataSuccess(baseObjectBean);
                    }
                },
                new Response.ErrorListener() {
                    //请求失败执行这个方法
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //错误信息,异常信息
                        Log.d("TAG", "请求失败" + volleyError.getMessage(), volleyError);
                        //失败的接口回调
                        onBestNewListener.requestBestNewDataFailed(volleyError);
                    }
                });
        //设置添加到请求队列
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
