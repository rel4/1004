package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.WinEnsureAddressBean;
import com.hongbaogou.listener.OnWinEnsureAddressListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class WinEnsureAddressRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/do_confirm_address?";
    public void winEnsureAddressRequests(String uid, String rid,String aid, final OnWinEnsureAddressListener onWinEnsureAddressListener){
        mQueue = RequestManager.getRequestQueue();
        String url =  urlBase+urlRequest+getParams()+"&uid="+uid+"&rid="+rid+"+&aid="+aid;
       // url =   "http://192.168.1.50/index.php/yungouapi/member/do_upload_touimg";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);
                        WinEnsureAddressBean winEnsureAddressBean = JSON.parseObject(substring,WinEnsureAddressBean.class);
                        onWinEnsureAddressListener.OnWinEnsureAddressListenerSuccess(winEnsureAddressBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGS", error.getMessage(), error);
                onWinEnsureAddressListener.OnWinEnsureAddressListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }
}
