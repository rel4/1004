package com.hongbaogou.listener;

import com.android.volley.VolleyError;
import com.hongbaogou.bean.BaseListBean;

/**
 * Created by Administrator on 2015/12/1.
 */
public interface OnGuessYouLikeListListener {

    public void OnGuessYouLikeListListenerSuccess(BaseListBean baseListBean);

    public void OnGuessYouLikeListListenerFailed(VolleyError error);
}
