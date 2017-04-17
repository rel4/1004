package com.hongbaogou.view.headviewpage;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015/12/15.
 */
public class HeadViewPager extends ViewPager {


    /**
     * 记录按下的Y轴坐标
     */
    private int lastMotionX;
    //判断是否可以跳转页面
    private boolean isCanStart = false;

    private int position;
    private int size;
    private OnStartActivityListener onStartActivityListener;

    public void setOnStartActivityListener(OnStartActivityListener onStartActivityListener) {
        this.onStartActivityListener = onStartActivityListener;
    }

    public HeadViewPager(Context context) {
        super(context);
    }

    public HeadViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int height = 0;
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            int h = child.getMeasuredHeight();
//            if (h > height)
//                height = h;
//        }
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastMotionX = (int) event.getX();
                position = getCurrentItem();
                size = getAdapter().getCount();
                break;
            case MotionEvent.ACTION_MOVE:
                int X = (int) event.getX();
                if ( position == size- 2 && X - lastMotionX < -250) {
                    isCanStart = true;
                } else {
                    isCanStart = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                getCurrentItem();
                 if(isCanStart){
                     onStartActivityListener.toStart(position);
                     isCanStart = false;
                 }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    public interface OnStartActivityListener{
        void toStart(int position);
    }
}
