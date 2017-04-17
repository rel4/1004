package com.hongbaogou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.hongbaogou.bean.Result;
import com.hongbaogou.utils.Request;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by L.K.X on 2016/5/26.
 */
public abstract class BaseNetActivity extends FragmentActivity {
    private Handler handlerForNet = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Request.SUCCESS:
                    onSuccess((Result) msg.obj);
                    break;
                case Request.ERROR:
                    onError((Result) msg.obj);
                    break;
            }
        }
    };
    protected Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        request = new Request(handlerForNet);
    }
    public abstract void onSuccess(Result result);
    public abstract void onError(Result result);


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
