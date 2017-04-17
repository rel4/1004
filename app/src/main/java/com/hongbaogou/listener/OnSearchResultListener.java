package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseObjectBean;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface OnSearchResultListener {
    /**
     * 成功请求的回调
     * @param baseObjectBean bean對象
     */
    public void requestSearchResultSuccess(BaseObjectBean baseObjectBean);

    /**
     * 失败请求的回调
     *
     * @param error volley錯誤請求對象
     */
    public void requestSearchResultFailed(VolleyError error);

}
