package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.HeadImageBean;

public interface OnPersonalDataTouimgListener {

    public void OnPersonalDataTouimgListenerSuccess(HeadImageBean headImageBean);

    public void OnPersonalDataTouimgListenerFailed(VolleyError error);
}
