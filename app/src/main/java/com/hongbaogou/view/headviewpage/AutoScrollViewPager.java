package com.hongbaogou.view.headviewpage;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class AutoScrollViewPager extends ViewPager {

    /**
     * 记录按下的Y轴坐标
     */
    private int lastMotionY;
    //自动滑动的时间
    private long autoTurningTime;
    private boolean turning = false;

    public AutoScrollViewPager(Context context) {
        super(context);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取屏幕的分辨率
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;//屏幕宽　
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (screenWidth * 0.51), MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean onTouchEvent(MotionEvent event) {
        lastMotionY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopTurning();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                startTurning(10000);
                break;
            case MotionEvent.ACTION_CANCEL:
                startTurning(10000);
                break;
        }
        return super.onTouchEvent(event);
    }

    private Handler timeHandler = new Handler();
    private Runnable adSwitchTask = new Runnable() {
        public void run() {
            if (getAdapter() != null) {
                int page = getCurrentItem();
                if (page == getAdapter().getCount() - 1) {
                    page = 0;
                } else {
                    page = page + 1;
                }
                setCurrentItem(page);
                timeHandler.postDelayed(adSwitchTask, autoTurningTime);
            }
        }
    };

    /***
     * 停止开始翻页
     */
    public void stopTurning() {
        turning = false;
        timeHandler.removeCallbacks(adSwitchTask);
    }

    /***
     * 开始翻页
     *
     * @param autoTurningTime 自动翻页时间
     */
    public void startTurning(long autoTurningTime) {
        //如果是正在翻页的话先停掉
        if (turning) {
            stopTurning();
        }
        this.autoTurningTime = autoTurningTime;
        turning = true;
        timeHandler.postDelayed(adSwitchTask, autoTurningTime);
    }
}
