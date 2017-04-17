package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseListBean;

/**
 * Created by Administrator on 2015/12/1.
 */
public interface OnPersonDataAddressListener {

    public void OnPersonDataAddressListenerSuccess(BaseListBean baseListBean);

    public void OnPersonDataAddressListenerFailed(VolleyError error);
}
