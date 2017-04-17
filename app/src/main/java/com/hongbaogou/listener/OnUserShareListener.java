package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseListBean;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface OnUserShareListener {
    /**
     * 成功请求的回调
     */
    public void requestUserShareSuccess(BaseListBean baseListBean);

    /**
     * 失败请求的回调
     *
     * @param error
     */
    public void requestUserShareFailed(VolleyError error);

}
