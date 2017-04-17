package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseListBean;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface WinMessageListener {

    public void onWinMessageSuccess(BaseListBean baseListBean);

    public void onWinMessageFailed(VolleyError error);
}
