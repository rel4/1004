package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/12/9.
 */
public interface OnPayListener {

    public void onPaySuccess(BaseObjectBean baseObjectBean);

    public void onPayFailed(VolleyError error);
}
