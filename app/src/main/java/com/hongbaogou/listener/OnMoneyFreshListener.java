package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by intasect on 2016/11/21.
 */
public interface OnMoneyFreshListener {
    /**
     * 成功请求的回调
     */
    public void requestMoneyFreshSuccess(BaseObjectBean baseObjectBean);

    /**
     * 失败请求的回调
     *
     * @param error
     */
    public void requestMoneyFreshFailed(VolleyError error);
}
