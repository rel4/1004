package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/12/4.
 */
public interface OnShoppingCartDeleteListener {

    public void onShoppingCartDeleteSuccess(BaseObjectBean baseObjectBean);

    public void onShoppingCartDeleteFailed(VolleyError error);
}
