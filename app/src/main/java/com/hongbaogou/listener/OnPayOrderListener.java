package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/12/9.
 */
public interface OnPayOrderListener {

    public void onPayOrderSuccess(BaseObjectBean baseObjectBean);

    public void onPayOrderFailed(VolleyError error);
}
