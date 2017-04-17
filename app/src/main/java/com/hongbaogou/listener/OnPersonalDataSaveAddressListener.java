package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

public interface OnPersonalDataSaveAddressListener {

    public void OnPersonalDataSaveAddressListenerSuccess(BaseObjectBean baseObjectBean);

    public void OnPersonalDataSaveAddressListenerFailed(VolleyError error);
}
