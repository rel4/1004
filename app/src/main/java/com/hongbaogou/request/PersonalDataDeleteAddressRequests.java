package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.EmptyBean;
import com.hongbaogou.listener.OnPersonalDataDeleteAddressListener;
import com.hongbaogou.utils.RequestManager;

//删除地址请求
public class PersonalDataDeleteAddressRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/do_del_member_address?";
    public void personalDataDeleteAddressRequests(String uid,String id, final OnPersonalDataDeleteAddressListener onPersonalDataDeleteAddressListener){
        mQueue = RequestManager.getRequestQueue();
        String url = urlBase+urlRequest+getParams()+"&uid="+uid+"&id="+id;
  //      url =   "http://192.168.1.50/index.php/yungouapi/member/do_del_member_address?partner=ygios&timestamp=1448349477&sign=5e08952761374f670fb34d5a8bc7bee8&uid="+uid+"&id="+id;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring",substring );
                       BaseObjectBean baseObjectBean = JSON.parseObject(substring, new TypeReference<BaseObjectBean<EmptyBean>>() {});
                        onPersonalDataDeleteAddressListener.OnPersonalDataDeleteAddressListenerSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAGS", error.getMessage(), error);
                onPersonalDataDeleteAddressListener.OnPersonalDataDeleteAddressListenerFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }


}
