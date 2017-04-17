package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseListBean;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface OnBannerListener {

    public void onBannerSuccess(BaseListBean baseListBean);

    public void onBannerFailed(VolleyError error);

}
