package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.WinEnsureAddressBean;

/**
 * 加入购物车
 */
public interface OnWinEnsureAddressListener {
    public void OnWinEnsureAddressListenerSuccess(WinEnsureAddressBean winEnsureAddressBean);

    public void OnWinEnsureAddressListenerFailed(VolleyError error);
}
