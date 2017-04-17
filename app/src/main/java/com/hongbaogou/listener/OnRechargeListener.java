package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/12/1.
 */
public interface OnRechargeListener {

    public void OnRechargeListenerSuccess(BaseObjectBean baseObjectBean);

    public void OnRechargeListenerFailed(VolleyError error);
}
