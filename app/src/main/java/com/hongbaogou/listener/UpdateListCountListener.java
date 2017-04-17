package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2016/1/4.
 */
public interface UpdateListCountListener {

    public void onUpdateListCountSuccess(BaseObjectBean baseObjectBean);

    public void onUpdateListCountFailed(VolleyError error);

}
