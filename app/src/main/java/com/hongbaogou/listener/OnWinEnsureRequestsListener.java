package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

public interface OnWinEnsureRequestsListener {

    public void OnWinEnsureRequestsListenerSuccess(BaseObjectBean baseObjectBean);

    public void OOnWinEnsureRequestsListenerFailed(VolleyError error);
}
