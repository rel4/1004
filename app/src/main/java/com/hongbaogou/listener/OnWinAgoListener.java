package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/12/8.
 */
public interface OnWinAgoListener {

    public void onWinAgoSuccess(BaseObjectBean baseObjectBean);

    public void onWinAgoFailed(VolleyError error);
}
