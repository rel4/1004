package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.HeadImageBean;
import com.hongbaogou.listener.OnPersonalDataTouimgListener;
import com.hongbaogou.utils.RequestManager;

//确认收货
public class WinEnsureRequests extends BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "member/do_confirm_receiving?";
    public void winEnsureRequests(String uid,String rid, final OnPersonalDataTouimgListener onPersonalDataTouimgListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase+urlRequest+getParams()+"&uid="+uid+"&rid="+rid;
  //      url = "http://192.168.1.50/index.php/yungouapi/member/do_confirm_receiving?partner=ygios&timestamp=1448349477&sign=5e08952761374f670fb34d5a8bc7bee8&uid="+uid+"&rid="+rid;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        HeadImageBean headImageBean = JSON.parseObject(substring, HeadImageBean.class);
                        onPersonalDataTouimgListener.OnPersonalDataTouimgListenerSuccess(headImageBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGS", error.getMessage(), error);
                onPersonalDataTouimgListener.OnPersonalDataTouimgListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }


}
