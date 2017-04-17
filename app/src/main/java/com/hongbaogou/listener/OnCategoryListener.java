package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseListBean;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface OnCategoryListener {

    public void onCategorySuccess(BaseListBean baseListBean);

    public void onCategoryFailed(VolleyError error);
}


