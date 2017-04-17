package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

public interface OnPersonalDataListener {

    public void OnPersonalDataListenerSuccess(BaseObjectBean baseObjectBean);

    public void OnPersonalDataListeneroFailed(VolleyError error);
}
