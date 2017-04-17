package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseListBean;

/**
 * Created by Administrator on 2015/12/4.
 */
public interface OnShoppingCartListListener {

    public void onShoppingCartListSuccess(BaseListBean baseListBean);

    public void onShoppingCartListFailed(VolleyError error);
}
