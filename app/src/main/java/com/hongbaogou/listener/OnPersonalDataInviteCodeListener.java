package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

public interface OnPersonalDataInviteCodeListener {

    public void OnPersonalDataInviteCodeListenerSuccess(BaseObjectBean baseObjectBean);

    public void OnPersonalDataInviteCodeListenerFailed(VolleyError error);
}
