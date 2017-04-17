package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.PersonDataBean;
import com.hongbaogou.listener.OnPersonalDataSMSListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class PersonalDataSMSRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/send_mod_mobile_code?";
    public void personalDatasmsRequests(String uid,String mobile,String bind_type, final OnPersonalDataSMSListener onPersonalDataSMSListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase+urlRequest+getParams()+"&uid="+uid+"&mobile="+mobile+"&bind_type="+bind_type;

        System.out.println("------绑定手机url-------"+url);

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        System.out.println("------绑定手机response-------" + response);
                       BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<PersonDataBean>>() {
                       });
                        onPersonalDataSMSListener.OnPersonalDataSMSListenerSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGS", error.getMessage(), error);
                onPersonalDataSMSListener.OnPersonalDataSMSListeneroFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }
}
