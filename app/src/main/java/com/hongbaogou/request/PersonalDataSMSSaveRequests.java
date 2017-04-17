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
import com.hongbaogou.listener.OnPersonalDataSMSSaveListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class PersonalDataSMSSaveRequests extends BaseRequest{

    private RequestQueue mQueue;
    private String urlRequest = "member/do_save_mobile?";
    public void personalDatasmsSaveRequests(String uid,String old_mobile,String mobile,String code,String bind_type, final OnPersonalDataSMSSaveListener onPersonalDataSMSSaveListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase+urlRequest+getParams()+"&uid="+uid+"&old_mobile="+old_mobile+"&mobile="+mobile+"&code="+code+"&bind_type="+bind_type;

        System.out.println("------保存手机号url------"+url);

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        System.out.println("------保存手机号response------" + response);
                       BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<PersonDataBean>>() {
                       });
                        onPersonalDataSMSSaveListener.OnPersonalDataSMSSaveListenerSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGS", error.getMessage(), error);
                onPersonalDataSMSSaveListener.OnPersonalDataSMSSaveListeneroFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }


}
