package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

public interface OnPersonalDataNicknameListener {

    public void OnPersonalDataNicknameListenerSuccess(BaseObjectBean baseObjectBean);

    public void OnPersonalDataNicknameListeneroFailed(VolleyError error);
}
