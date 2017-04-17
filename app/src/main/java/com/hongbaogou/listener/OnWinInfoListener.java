package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/12/7.
 */
public interface OnWinInfoListener {

    public void onWinInfoSuccess(BaseObjectBean baseObjectBean,int position);

    public void onWinInfoFailed(VolleyError error);
}


