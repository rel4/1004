package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.WinMessageListBean;
import com.hongbaogou.listener.WinMessageListener;
import com.hongbaogou.utils.RequestManager;

/**
 * 主页中奖消息滚动请求
 */
public class WinMessageRequest extends  BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "index/get_lottery_list?";


    public void winMessageRequest(final WinMessageListener winMessageListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase + urlRequest + getParams();
        Log.d("url", url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {

                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);

                        BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<WinMessageListBean>>() {});
                        winMessageListener.onWinMessageSuccess(baseListBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                winMessageListener.onWinMessageFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    };

}
