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
import com.hongbaogou.listener.OnPersonalDataListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class PersonalDataRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/get_member_center?";
    public void personalDataRequests(String uid,final OnPersonalDataListener onPersonalDataListener){
        mQueue = RequestManager.getRequestQueue();
        String url =   urlBase+urlRequest+getParams()+"&uid="+uid;
  //              "http://192.168.1.50/index.php/yungouapi/member/get_member_center?partner=ygios&timestamp=1448349477&sign=5e08952761374f670fb34d5a8bc7bee8&uid="+uid;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                       BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<PersonDataBean>>() {});
                        onPersonalDataListener.OnPersonalDataListenerSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGS", error.getMessage(), error);
                onPersonalDataListener.OnPersonalDataListeneroFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }
}
