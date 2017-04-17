package com.hongbaogou.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.hongbaogou.R;

/**
 * Created by Administrator on 2015/12/25.
 */
public class PopWindowUtils {

    /**
     * 初始化popwindow布局
     *
     * @param Context
     * @return
     */
    public static View initPopwindowLayout(Context Context) {
        LayoutInflater inflater = LayoutInflater.from(Context);
        View view = inflater.inflate(R.layout.popupwindow, null, false);
        return view;
    }

    /**
     * 创建popwindow
     *
     * @param popLayout popwindow的布局
     * @return pop对象
     */
    public static PopupWindow creatPopuptWindow(View popLayout) {
        // 创建PopupWindow实例
        final PopupWindow popupWindow = new PopupWindow(popLayout, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
        return popupWindow;
    }
}
