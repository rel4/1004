package com.hongbaogou.fragment;


import android.support.v4.app.Fragment;
import android.view.View;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;


public class BaseFragment extends Fragment implements ScrollableHelper.ScrollableContainer {

    public boolean initRequest() {
        return true;
    }
    //更新数据
    public void freshDate(){}
    public void clearCart(){}

    @Override
    public View getScrollableView() {
        return null;
    }


    public void onRefreshing(){};
}
