package com.hongbaogou.fragment;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.hongbaogou.bean.Result;
import com.hongbaogou.utils.Request;


/**
 * Created by L.K.X on 2016/5/26.
 */
public abstract class BaseNetFragment extends Fragment {
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        request = new Request(handlerForNet);
    }

    public abstract void onSuccess(Result result);

    public abstract void onError(Result result);
}
