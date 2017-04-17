package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/12/1.
 */
public interface OnRewardDetailListener {

    public void OnRewardDetailListenerSuccess(BaseObjectBean baseObjectBean);

    public void OnRewardDetailListenerFailed(VolleyError error);
}
