package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

public interface OnWinRecordsInfoListener {

    public void OnWinRecordsInfoSuccess(BaseObjectBean baseObjectBean);

    public void OnWinRecordsInfoFailed(VolleyError error);
}
