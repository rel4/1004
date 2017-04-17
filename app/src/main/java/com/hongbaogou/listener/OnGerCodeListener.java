package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface OnGerCodeListener {
    /**
     * 成功请求的回调
     */
    public void requestGerCodeSuccess(BaseObjectBean baseObjectBean);

    /**
     * 失败请求的回调
     * @param error
     */
    public void requestGerCodeFailed(VolleyError error);

}
