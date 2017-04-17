package com.hongbaogou.listener;

import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2015/12/1.
 */
public interface OnPayByHCListener {

    public void OnPayByHCListenerSuccess(String jsonString);

    public void OnPayByHCListenerFailed(VolleyError error);
}
