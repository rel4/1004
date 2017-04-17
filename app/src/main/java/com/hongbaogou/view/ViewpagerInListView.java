package com.hongbaogou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Administrator on 2015/12/23.
 */
public class ViewpagerInListView extends ListView {

    public ViewpagerInListView(Context context) {
        super(context);
    }

    public ViewpagerInListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int mLastMotionX;
    private int mLastMotionY;
    private boolean isTouchOnPage = false;
    private OnTouchInViewListener onTouchInViewListener;

    public void setOnTouchInViewListener(OnTouchInViewListener onTouchInViewListener) {
        this.onTouchInViewListener = onTouchInViewListener;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        Log.e("TAG","x =============================" + x);
        Log.e("TAG","y =============================" + y);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = (int) ev.getX();
                mLastMotionY = (int) ev.getY();
                int position = pointToPosition(mLastMotionX, mLastMotionY);
                if (position == 0) {
                    if (onTouchInViewListener != null) {
                        isTouchOnPage = isTouchPointInView(onTouchInViewListener.getTouchView(), mLastMotionX, mLastMotionY);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isTouchOnPage) {
                    int deltaX = x - mLastMotionX;
                    int deltaY = y - mLastMotionY;
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(ev);

    }

    private boolean isTouchPointInView(View view, int x, int y) {
        if (y >= view.getTop() && y <= view.getBottom()
                && x >= view.getLeft() && x <= view.getRight()) {
            return true;
        }
        return false;
    }

    public interface OnTouchInViewListener {
        public View getTouchView();
    }
}
