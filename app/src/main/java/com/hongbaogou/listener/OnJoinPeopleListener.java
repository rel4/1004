package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/11/29.
 */
public interface OnJoinPeopleListener {

    public void onJoinPeopleSuccess(BaseObjectBean baseObjectBean);

    public void onJoinPeopleFailed(VolleyError error);


}
