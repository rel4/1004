package com.hongbaogou.request;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.listener.OnPayByHCListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class PayByHCRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "cart/wap_add_money?";

    public void payByHCRequests(String uid,String money,String paytype,final OnPayByHCListener onPayByHCListener){
        mQueue = RequestManager.getRequestQueue();

        String url=urlBase+urlRequest+getParams()+"&uid="+uid+"&money="+money+"&pay_type="+paytype;

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
//                        BaseObjectBean baseObjectBean = JSON.parseObject(response,new TypeReference<BaseObjectBean<RechargeBean>>(){});
                        onPayByHCListener.OnPayByHCListenerSuccess(substring);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGW", error.getMessage(), error);
                onPayByHCListener.OnPayByHCListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest, mQueue);
    }
}
