package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

public interface OnPersonalDataDeleteAddressListener {

    public void OnPersonalDataDeleteAddressListenerSuccess(BaseObjectBean baseObjectBean);

    public void OnPersonalDataDeleteAddressListenerFailed(VolleyError error);
}
