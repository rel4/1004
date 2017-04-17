package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.GuessYouLikeBean;
import com.hongbaogou.listener.OnGuessYouLikeListListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class GuessYouLikeRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/get_guess_you_like?";
    public void guessYouLikeRequests(final OnGuessYouLikeListListener onGuessYouLikeListListener){
        mQueue = RequestManager.getRequestQueue();
        String url =  urlBase + urlRequest + getParams() ;
       // url =   "http://192.168.1.50/index.php/yungouapi/member/get_guess_you_like?partner=ygios&timestamp=1448349477&sign=5e08952761374f670fb34d5a8bc7bee8";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {

                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                       BaseListBean baseListBean = JSON.parseObject(substring, new TypeReference<BaseListBean<GuessYouLikeBean>>() {});

                        onGuessYouLikeListListener.OnGuessYouLikeListListenerSuccess(baseListBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onGuessYouLikeListListener.OnGuessYouLikeListListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }
}
