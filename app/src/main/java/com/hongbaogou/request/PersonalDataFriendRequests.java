package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.PersonalDataFriendBean;
import com.hongbaogou.listener.OnPersonalDataFriendListener;
import com.hongbaogou.utils.RequestManager;


public class PersonalDataFriendRequests extends  BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/get_member_friendship?";
    public void personalDataFriendRequests(String uid,final OnPersonalDataFriendListener onPersonalDataFriendListener){
        mQueue = RequestManager.getRequestQueue();
        String url =urlBase+urlRequest+getParams()+"&uid="+uid;
        //       "http://192.168.1.50/index.php/yungouapi/member/get_member_friendship?partner=ygios&timestamp=1448349477&sign=5e08952761374f670fb34d5a8bc7bee8&uid="+uid;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                        BaseObjectBean bseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<PersonalDataFriendBean>>() {});
                        onPersonalDataFriendListener.OnPersonalDataFriendListenerSuccess(bseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGS", error.getMessage(), error);
                onPersonalDataFriendListener.OnPersonalDataFriendListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }
}
