package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface OnGoodsListener {

    public void onGoodsSuccess(BaseObjectBean baseObjectBean);

    public void onGoodsFailed(VolleyError error);
}
