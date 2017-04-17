package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BeanKey;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface OnShareKeyListener {
    /**
     * 成功请求的回调
     */
    public void requestShareKeySuccess(BeanKey beanKey);


    /**
     * 失败请求的回调
     *
     * @param error
     */
    public void requestShareKeyFailed(VolleyError error);

}
