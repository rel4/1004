package com.hongbaogou.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.WinRecordsInfoBean;
import com.hongbaogou.listener.OnWinRecordsInfoListener;
import com.hongbaogou.utils.RequestManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class WinRecordsInfoRequests extends BaseRequest {

    private RequestQueue mQueue;
    private String urlRequest = "member/get_win_records_info?";
    public void payRecodersRequest(String uid ,String id,final OnWinRecordsInfoListener onWinRecordsInfoListener){
        mQueue = RequestManager.getRequestQueue();

        //String url =  "http://192.168.1.50/index.php/yungouapi/member/get_win_records_info?partner=ygios&timestamp=1448349477&sign=5e08952761374f670fb34d5a8bc7bee8&id="+id+"&uid="+uid;
        String url = urlBase+urlRequest+getParams()+"&id="+id+"&uid="+uid;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        String substring = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        Log.d("substring", substring);

                        BaseObjectBean baseObjectBean = JSON.parseObject(substring,new TypeReference<BaseObjectBean<WinRecordsInfoBean>>(){});

                        onWinRecordsInfoListener.OnWinRecordsInfoSuccess(baseObjectBean);
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                onWinRecordsInfoListener.OnWinRecordsInfoFailed(error);
            }
        });
        RequestManager.addRequest(stringRequest,mQueue);
    }
}
