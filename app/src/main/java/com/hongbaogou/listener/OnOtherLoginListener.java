package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface OnOtherLoginListener {
    /**
     * 成功请求的回调
     */
    public void requestOnOtherLoginSuccess(BaseObjectBean baseObjectBean);

    /**
     * 失败请求的回调
     * @param error
     */
    public void requestOnOtherLoginFailed(VolleyError error);

}
