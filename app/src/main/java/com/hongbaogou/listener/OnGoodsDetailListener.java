package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/11/30.
 */
public interface OnGoodsDetailListener {

    public void onGoodsDetailSuccess(BaseObjectBean baseObjectBean);

    public void onGoodsDetailFailed(VolleyError error);
}
