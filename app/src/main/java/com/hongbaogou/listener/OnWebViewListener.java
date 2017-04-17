package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

public interface OnWebViewListener {

    public void OnWebViewListenerSuccess(BaseObjectBean baseObjectBean);

    public void OnWebViewListenerFailed(VolleyError error);
}
