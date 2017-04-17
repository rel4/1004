package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/12/29.
 */
public interface OnOrderCodeListener {

    public void onOrderCodeSuccess(BaseObjectBean baseObjectBean);

    public void onOrderCodeFailed(VolleyError error);
}
