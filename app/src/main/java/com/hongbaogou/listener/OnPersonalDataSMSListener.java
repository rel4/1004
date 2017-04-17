package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

public interface OnPersonalDataSMSListener {

    public void OnPersonalDataSMSListenerSuccess(BaseObjectBean baseObjectBean);

    public void OnPersonalDataSMSListeneroFailed(VolleyError error);
}
