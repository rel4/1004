package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.MessageCountBean;

/**
 * 加入购物车
 */
public interface OnAddShopCartListener {
    public void onAddShopCartSuccess(MessageCountBean messageCountBean);

    public void onAddShopCartFailed(VolleyError error);
}
