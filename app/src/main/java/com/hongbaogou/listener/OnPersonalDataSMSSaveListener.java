package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

public interface OnPersonalDataSMSSaveListener {

    public void OnPersonalDataSMSSaveListenerSuccess(BaseObjectBean baseObjectBean);

    public void OnPersonalDataSMSSaveListeneroFailed(VolleyError error);
}
