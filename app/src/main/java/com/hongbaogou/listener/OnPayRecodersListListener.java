package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseListBean;

/**
 * Created by Administrator on 2015/12/1.
 */
public interface OnPayRecodersListListener {

    public void OnPayRecodersListListenerSuccess(BaseListBean baseListBean);

    public void OnPayRecodersListListenerFailed(VolleyError error);
}
