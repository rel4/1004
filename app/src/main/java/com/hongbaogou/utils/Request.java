package com.hongbaogou.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.hongbaogou.bean.Result;
import com.hongbaogou.request.BaseRequest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by L.K.X on 2016/5/26.
 */
public class Request extends BaseRequest {
    private Handler handler;
    private RequestQueue mQueue;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;

    public Request(Handler handler) {
        this.handler = handler;
        mQueue = RequestManager.getRequestQueue();
    }

    public void get(final String tag, String url, String requestEntity) {
        String totalUrl = urlBase + url + getParams() + requestEntity;
        System.out.println("requestUrl-------" + totalUrl);
        StringRequest stringRequest = new StringRequest(totalUrl,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        System.out.println("result:--------" + response);

                        Result result = new Result();
                        result.tag = tag;
                        result.result = response;

                        Message msg = Message.obtain();
                        msg.what = SUCCESS;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Result result = new Result();
                result.tag = tag;
                result.result = error.getMessage();

                Message msg = Message.obtain();
                msg.what = ERROR;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
        RequestManager.addRequest(stringRequest, mQueue);
    }


   public void okHttpBy_get(String request_tag, String middle_url,String request_param__url){
       String finalUrl = urlBase + middle_url + getParams() + request_param__url;
       System.out.println("requestUrl-------" + finalUrl);
       OkHttpUtils
               .get()
               .url(finalUrl)
               .id(100)
               .build()
               .execute(new MyStringCallback(request_tag));
   }


    public void okHttpBy_post(String request_tag, String middle_url,Object objectBean){
        String finalUrl = urlBase + middle_url + getParams();
        System.out.println("requestUrl-------" + finalUrl);
        OkHttpUtils
                .postString()
                .url(finalUrl)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(new Gson().toJson(objectBean))
                .build()
                .execute(new MyStringCallback(request_tag));
    }


    public class MyStringCallback extends StringCallback
    {
        String request_tag;
        public MyStringCallback(String request_tag) {
            this.request_tag = request_tag;
        }

        @Override
        public void onBefore(okhttp3.Request request, int id)
        {
//            setTitle("loading...");
        }


        @Override
        public void inProgress(float progress, long total, int id)
        {
//            Log.e(TAG, "inProgress:" + progress);
//            mProgressBar.setProgress((int) (100 * progress));
        }

        @Override
        public void onAfter(int id)
        {
//            setTitle("Sample-okHttp");
        }

        @Override
        public void onResponse(String response, int id)
        {
            System.out.println("result:--------" + response);

            switch (id)
            {
                case 100:
//                    Toast.makeText(MainActivity.this, "http", Toast.LENGTH_SHORT).show();
                    Result result = new Result();
                    result.tag = request_tag;
                    result.result = response;

                    Message msg = Message.obtain();
                    msg.what = SUCCESS;
                    msg.obj = result;
                    handler.sendMessage(msg);
                    break;
                case 101:
//                    Toast.makeText(MainActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            Log.e("TAG", e.getMessage(), e);
            Result result = new Result();
            result.tag = request_tag;
            result.result = e.getMessage();

            Message msg = Message.obtain();
            msg.what = ERROR;
            msg.obj = result;
            handler.sendMessage(msg);
            e.printStackTrace();
        }

    }


    public void getImage(final String request_tag, String middle_url,String request_param__url)
    {
        String finalUrl = urlBase + middle_url + getParams() + request_param__url;
        System.out.println("requestUrl-------" + finalUrl);
        OkHttpUtils
                .get()//
                .url(finalUrl)//
                .tag(this)//
                .build()//
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(new BitmapCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Log.e("TAG", e.getMessage(), e);
                        Result result = new Result();
                        result.tag = request_tag;
                        result.result = e.getMessage();

                        Message msg = Message.obtain();
                        msg.what = ERROR;
                        msg.obj = result;
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id)
                    {
                        Result result = new Result();
                        result.tag = request_tag;
                        result.bitmap = bitmap;

                        Message msg = Message.obtain();
                        msg.what = SUCCESS;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }


}
